package com.legendmohe;

import java.util.Map;

import pipeline.CounterAction;
import pipeline.EnumAction;
import pipeline.InterceptAction;
import pipeline.StatisticPipeLine;
import pipeline.TimerAction;

public class Main {

    public static void main(String[] args) {
        // 标记一些动作
        // 默认提供了开始-结束计时动作(TimerAction)、计数器动作(CounterAction)、枚举动作(EnumAction)
        // TODO - name字符串全部改成MainStat的常量
        MainStat.pipeline()
                .put(EnumAction.fromValue(5), "entrance")
                .put(CounterAction.fromZero(), "counter")
                .put(TimerAction.Start.fromCurrentTimestamp(), "start_click")
        ;

        // 模拟耗时操作
        sleep(2000);

        // 结束计时
        MainStat.pipeline().put(TimerAction.End.fromStart("start_click"), "show_ui");
        // 收集平均值
        MainStat.pipeline().put(TimerAction.Avg.collect("start_click"), "show_ui_avg");

        // 计数器+1
        MainStat.pipeline().put(CounterAction.increase("counter")); // should print counter=1

        // 再次点击
        MainStat.pipeline().put(TimerAction.Start.fromCurrentTimestamp(), "start_click");

        // 模拟耗时操作
        sleep(1000);

        // 结束计时
        MainStat.pipeline().put(TimerAction.End.fromStart("start_click"), "show_video");
        // 收集平均值
        MainStat.pipeline().put(TimerAction.Avg.collect("start_click"), "show_ui_avg");

        // 结束统计，发送统计数据
        MainStat.sendStat();
    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    这里是业务统计类。
    不同业务可以新建自己的统计类。使用静态变量是为了方便跨模块标记打点。
     */
    private static class MainStat {
        private static StatisticPipeLine sPipeLine = StatisticPipeLine.create("game_statistic");

        public static StatisticPipeLine pipeline() {
            return sPipeLine;
        }

        public static void sendStat() {
            // 可以对结果进行处理
            sPipeLine.put(new InterceptAction() {
                @Override
                public void onCalculate(StatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result) {
                    result.put("total_result_count", result.size());
                }
            });

            // 获得统计结果
            Map<String, Object> result = sPipeLine.calculate();
            Map<String, Object> result2 = sPipeLine.calculate();

            // 重置
            sPipeLine.reset();

            // 多次calculate结果应该是一样的
            if (!result.toString().equals(result2.toString())) {
                throw new IllegalStateException("calculate 不一致");
            } else {
                System.out.println(result);
            }
        }
    }
}
