package com.random.carryyou.service.impl;

import cn.hutool.core.date.StopWatch;
import com.random.carryyou.dto.ObjectDetectionResult;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_dnn;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_dnn.Net;
import org.bytedeco.opencv.opencv_text.FloatVector;
import org.bytedeco.opencv.opencv_text.IntVector;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_dnn.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_SIMPLEX;

public class FFMPEGTest {

    static String detectImageSuffix = "dec";

    static String cfgPath = "/Users/lozhu/Documents/projects/carryyou/carryyou-backend/src/main/resources/yolo/yolov4.cfg";

    static String weightsPath = "/Users/lozhu/Documents/projects/carryyou/carryyou-backend/src/main/resources/yolo/yolov4.weights";

    static String namesPath = "/Users/lozhu/Documents/projects/carryyou/carryyou-backend/src/main/resources/yolo/coco.names";

    static int width = 608;

    static int height = 608;

    /**
     * 置信度门限（超过这个值才认为是可信的推理结果）
     */
    static final float CONFIDENCE_THRESHOLD = 0.5F;

    static final float NMS_THRESHOLD = 0.4F;

    // 神经网络
    static Net net;

    // 输出层
    static StringVector outNames;

    // 分类名称
    static List<String> names;

    public static void main(String[] args) throws Exception {
        test2();
    }

    public static void test1() throws Exception {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();

        CanvasFrame canvasFrame = new CanvasFrame("摄像头预览");
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (canvasFrame.isDisplayable()) {
            canvasFrame.showImage(grabber.grab());
        }

        grabber.close();
    }

    public static void test2() throws Exception {

        FFMPEGTest test = new FFMPEGTest();

        String videoPath = "//Users/lozhu/Documents/projects/carryyou/carryyou-backend/images/2024/1/01/2eba5d5d-91e4-46fd-9305-f8e6c862754b.mp4";
        FileInputStream fileInputStream = new FileInputStream(videoPath);
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(fileInputStream, 0);

        grabber.start();
        double frameRate = grabber.getFrameRate();

//        CanvasFrame canvasFrame = new CanvasFrame("本地视频预览");
//        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        canvasFrame.setResizable(true);

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("aa.mp4", grabber.getImageWidth(),
                grabber.getImageHeight(), 0);
        recorder.setFormat("mp4");
        recorder.setVideoQuality(0);
        recorder.setFrameRate(frameRate / 10);
        recorder.start();

        Frame frame = grabber.grabImage();
        int frameNumber;
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
//        while (canvasFrame.isDisplayable()) {
        while (frame != null) {
//            if (frame == null) {
//                break;
//            }
            frameNumber = grabber.getFrameNumber();
//            canvasFrame.showImage(frame);
            if (frameNumber % 10 == 0) {
                Mat mat = converter.convertToMat(frame);
                Mat mat1 = test.detectImage(mat);
                Frame frame1 = converter.convert(mat1);
                recorder.record(frame1);
            }
            Thread.sleep((long) (1000 / (frameRate * 2)));
            frame = grabber.grabImage();
        }

        grabber.close();
//        canvasFrame.dispose();
        recorder.flush();
        recorder.stop();
    }

    public Mat detectImage(Mat src) throws Exception {
        StopWatch stopWatch = new StopWatch("目标检测耗时计时");
        stopWatch.start("初始化神经网络");
        // 神经网络初始化
        net = readNetFromDarknet(cfgPath, weightsPath);
        stopWatch.stop();
        // 检查网络是否为空
        if (net.empty()) {
            throw new Exception("神经网络初始化失败");
        }

        stopWatch.start("输出层");
        // 输出层
        outNames = net.getUnconnectedOutLayersNames();
        stopWatch.stop();

        // 检查GPU
        if (getCudaEnabledDeviceCount() > 0) {
            net.setPreferableBackend(opencv_dnn.DNN_BACKEND_CUDA);
            net.setPreferableTarget(opencv_dnn.DNN_TARGET_CUDA);
        }

        // 分类名称
        try {
            names = Files.readAllLines(Paths.get(namesPath));
        } catch (IOException e) {
        }

        // 读取文件到Mat
//        Mat src = imread(filePath);

        // 执行推理
        stopWatch.start("执行推理");
        MatVector outs = doPredict(src);
        stopWatch.stop();

        // 处理原始的推理结果，
        // 对检测到的每个目标，找出置信度最高的类别作为改目标的类别，
        // 还要找出每个目标的位置，这些信息都保存在ObjectDetectionResult对象中
        stopWatch.start("处理原始的推理结果");
        List<ObjectDetectionResult> results = postprocess(src, outs);
        stopWatch.stop();

        // 释放资源
        outs.releaseReference();

        // 检测到的目标总数
        int detectNum = results.size();

        System.out.println("一共检测到 " + detectNum + " 个目标");

        // 计算出总耗时，并输出在图片的左上角
        stopWatch.start("打印总耗时");
        printTimeUsed(src);
        stopWatch.stop();

        // 将每一个被识别的对象在图片框出来，并在框的左上角标注该对象的类别
        stopWatch.start("标注识别对象");
        markEveryDetectObject(src, results);
        stopWatch.stop();

        // 将添加了标注的图片保持在磁盘上，并将图片信息写入map（给跳转页面使用）
//        saveMarkedImage(src, filePath);

        System.out.println("图片检测结束");
        String timeInfo = stopWatch.prettyPrint(TimeUnit.MILLISECONDS);
        System.out.println(timeInfo);

        return src;
    }

    private void printTimeUsed(Mat src) {
        // 总次数
        long totalNums = net.getPerfProfile(new DoublePointer());
        // 频率
        double freq = getTickFrequency() / 1000;
        // 总次数除以频率就是总耗时
        double t = totalNums / freq;

        // 将本次检测的总耗时打印在展示图像的左上角
        putText(src,
                String.format("Inference time : %.2f ms", t),
                new Point(10, 20),
                FONT_HERSHEY_SIMPLEX,
                0.6,
                new Scalar(255, 0, 0, 0),
                1,
                LINE_AA,
                false);
    }

    private void markEveryDetectObject(Mat src, List<ObjectDetectionResult> results) {
        // 在图片上标出每个目标以及类别和置信度
        for (ObjectDetectionResult result : results) {
            System.out.println("类别[{" + result.getClassName() + "}]，置信度[{" + result.getConfidence() * 100f + "}%]");

            // annotate on image
            rectangle(src,
                    new Point(result.getX(), result.getY()),
                    new Point(result.getX() + result.getWidth(), result.getY() + result.getHeight()),
                    Scalar.RED,
                    1,
                    LINE_4,
                    0);

            // 写在目标左上角的内容：类别+置信度
            String label = result.getClassName() + ": " + String.format("%.2f%%", result.getConfidence() * 100f);

            // 计算显示这些内容所需的高度
            IntPointer baseLine = new IntPointer();

            Size labelSize = getTextSize(label, FONT_HERSHEY_SIMPLEX, 0.5, 1, baseLine);
            int top = Math.max(result.getY(), labelSize.height());

            // 添加内容到图片上
            putText(src, label, new Point(result.getX(), top), FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0, 0), 1, LINE_4, false);
        }
    }

    private MatVector doPredict(Mat src) {
        // 将图片转为四维blog，并且对尺寸做调整
        Mat inputBlob = blobFromImage(src,
                1 / 255.0,
                new Size(width, height),
                new Scalar(0.0),
                true,
                false,
                CV_32F);

        // 神经网络输入
        net.setInput(inputBlob);

        // 设置输出结果保存的容器
        MatVector outs = new MatVector(outNames.size());

        // 推理，结果保存在outs中
        net.forward(outs, outNames);

        // 释放资源
        inputBlob.release();

        return outs;
    }

    private List<ObjectDetectionResult> postprocess(Mat frame, MatVector outs) {
        final IntVector classIds = new IntVector();
        final FloatVector confidences = new FloatVector();
        final RectVector boxes = new RectVector();

        // 处理神经网络的输出结果
        for (int i = 0; i < outs.size(); ++i) {
            // extract the bounding boxes that have a high enough score
            // and assign their highest confidence class prediction.

            // 每个检测到的物体，都有对应的每种类型的置信度，取最高的那种
            // 例如检车到猫的置信度百分之九十，狗的置信度百分之八十，那就认为是猫
            Mat result = outs.get(i);
            FloatIndexer data = result.createIndexer();

            // 将检测结果看做一个表格，
            // 每一行表示一个物体，
            // 前面四列表示这个物体的坐标，后面的每一列，表示这个物体在某个类别上的置信度，
            // 每行都是从第五列开始遍历，找到最大值以及对应的列号，
            for (int j = 0; j < result.rows(); j++) {
                // minMaxLoc implemented in java because it is 1D
                int maxIndex = -1;
                float maxScore = Float.MIN_VALUE;
                for (int k = 5; k < result.cols(); k++) {
                    float score = data.get(j, k);
                    if (score > maxScore) {
                        maxScore = score;
                        maxIndex = k - 5;
                    }
                }

                // 如果最大值大于之前设定的置信度门限，就表示可以确定是这类物体了，
                // 然后就把这个物体相关的识别信息保存下来，要保存的信息有：类别、置信度、坐标
                if (maxScore > CONFIDENCE_THRESHOLD) {
                    int centerX = (int) (data.get(j, 0) * frame.cols());
                    int centerY = (int) (data.get(j, 1) * frame.rows());
                    int width = (int) (data.get(j, 2) * frame.cols());
                    int height = (int) (data.get(j, 3) * frame.rows());
                    int left = centerX - width / 2;
                    int top = centerY - height / 2;

                    // 保存类别
                    classIds.push_back(maxIndex);
                    // 保存置信度
                    confidences.push_back(maxScore);
                    // 保存坐标
                    boxes.push_back(new Rect(left, top, width, height));
                }
            }

            // 资源释放
            data.release();
            result.release();
        }

        // remove overlapping bounding boxes with NMS
        IntPointer indices = new IntPointer(confidences.size());
        FloatPointer confidencesPointer = new FloatPointer(confidences.size());
        confidencesPointer.put(confidences.get());

        // 非极大值抑制
        NMSBoxes(boxes, confidencesPointer, CONFIDENCE_THRESHOLD, NMS_THRESHOLD, indices, 1.f, 0);

        // 将检测结果放入BO对象中，便于业务处理
        List<ObjectDetectionResult> detections = new ArrayList<>();
        for (int i = 0; i < indices.limit(); ++i) {
            final int idx = indices.get(i);
            final Rect box = boxes.get(idx);

            final int clsId = classIds.get(idx);

            detections.add(new ObjectDetectionResult(
                    clsId,
                    names.get(clsId),
                    confidences.get(idx),
                    box.x(),
                    box.y(),
                    box.width(),
                    box.height()
            ));

            // 释放资源
            box.releaseReference();
        }

        // 释放资源
        indices.releaseReference();
        confidencesPointer.releaseReference();
        classIds.releaseReference();
        confidences.releaseReference();
        boxes.releaseReference();

        return detections;
    }
}
