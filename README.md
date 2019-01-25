# StatisticPipeLine

``` Java

/**
 * 这里提供了一个使用StatisticPipeLine的使用例子
 * 使用StatisticPipeLine的好处有：
 * 1. 统一统计逻辑
 * 2. 统一代码风格
 * 另外，StatisticPipeLine设计上也很方便根据业务需求扩展统计能力。
 */
public class Main {

    public static void main(String[] args) {
        // 首先，新建一个stat类对pipeline进行单例化封装。
        // 然后可以开始利用pipeline的能力进行打点。
        // StatisticPipeLine默认提供了开始-结束计时动作(TimerAction)、计数器动作(CounterAction)、枚举动作(EnumAction)
        // TODO - name字符串全部改成MainStat的常量
        ExampleStat.pipeline()
                .put(EnumAction.fromValue(5), "entrance")
                .put(CounterAction.fromZero(), "counter")
                .put(TimerAction.Start.fromCurrentTimestamp(), "start_click")
        ;

        // 模拟耗时操作
        sleep(2000);

        // 结束计时
        ExampleStat.pipeline().put(TimerAction.End.fromStart("start_click"), "show_ui");
        // 收集平均值
        ExampleStat.pipeline().put(TimerAction.Avg.collect("start_click"), "show_ui_avg");

        // 计数器+1
        ExampleStat.pipeline().put(CounterAction.increase("counter")); // should print counter=1

        // 再次点击
        ExampleStat.pipeline().put(TimerAction.Start.fromCurrentTimestamp(), "start_click");

        // 模拟耗时操作
        sleep(1000);

        // 结束计时
        ExampleStat.pipeline().put(TimerAction.End.fromStart("start_click"), "show_video");
        // 收集平均值
        ExampleStat.pipeline().put(TimerAction.Avg.collect("start_click"), "show_ui_avg");

        // 可以清除指定name的action
//        MainStat.pipeline().put(RemoveAction.forName("show_ui_avg"));

        // 结束统计，发送统计数据
        ExampleStat.sendStat();
    }

    /*
    这里是业务统计类。
    不同业务可以新建自己的统计类。使用静态变量是为了方便跨模块标记打点。
     */
    private static class ExampleStat {
        private static IStatisticPipeLine sPipeLine = StatisticPipeLine.create("game_statistic");

        public static IStatisticPipeLine pipeline() {
            return sPipeLine;
        }

        public static void sendStat() {
            // 可以对已经处理了的结果再次进行处理
            sPipeLine.put(new InterceptAction() {
                @Override
                public void onCollect(IStatisticPipeLine pipeLine, Map<String, Object> context, Map<String, Object> result) {
                    result.put("total_result_count", result.size());
                }
            });

            // 获得统计结果
            Map<String, Object> result = sPipeLine.collect();
            Map<String, Object> result2 = sPipeLine.collect();

            // 重置，清空action
            sPipeLine.reset();

            // 多次calculate结果应该是一样的
            if (!result.toString().equals(result2.toString())) {
                throw new IllegalStateException("result 不一致");
            } else {
                // 模拟发送数据
                System.out.println(result);
            }
        }
    }

    ///////////////////////////////////private///////////////////////////////////

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```
