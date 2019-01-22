package com.legendmohe;

import java.util.Map;

import pipeline.CounterAction;
import pipeline.EnumAction;
import pipeline.InterceptAction;
import pipeline.StatisticPipeLine;
import pipeline.TimerAction;

public class Main {

    public static void main(String[] args) {
        // 1. 新建一个统计项
        StatisticPipeLine pipeLine = StatisticPipeLine.create("game_statistic");

        // 标记一些动作
        // 默认提供了开始-结束计时动作(TimerAction)、计数器动作(CounterAction)、枚举动作(EnumAction)
        pipeLine.put(EnumAction.fromValue(5), "entrance")
                .put(CounterAction.zero(), "counter")
                .put(TimerAction.Start.fromCurrentTimestamp(), "start_click");

        // 模拟耗时操作
        sleep(2000);

        // 结束计时
        pipeLine.put(TimerAction.End.fromStart("start_click"), "show_ui");

        // 计数器+1
        pipeLine.put(CounterAction.increase("counter")); // should print counter=1

        // 模拟耗时操作
        sleep(1000);

        // 结束计时
        pipeLine.put(TimerAction.End.fromStart("start_click"), "show_video");

        // 对结果进行处理
        pipeLine.put(new InterceptAction() {

            @Override
            public void onAssemble(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result) {
                result.put("other", "hahaha");
            }
        });

        // 获得统计结果
        Map<String, Object> assemble = pipeLine.assemble();

        // 结果为 {show_video=3005, counter=1, entrance=5, show_ui=2002}
        System.out.println(assemble);
    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
