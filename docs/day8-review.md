# Day 8 复盘

2026-11-09（北京时间）

## 学习者原始复盘

> 今天我亲自完成了：网关到派单服务到备件服务的微服务链
>
> 我最容易混淆的是：没有
>
> 下次我想独立重做或复习的是：线程的状态和这几个服务是如何串联起来的

## 项目进展与验证

- 梳理了报修、派单、接单、完工和验收的状态顺序，列出了工单字段和接口草案。调度员只能派本校区的工单，只有被指派的维修员可以接单，本校区拥有验收角色的用户可以验收处于待验收状态的工单。
- 在 Nacos 3.1.1 中注册了 `api-gateway`、`work-order-service` 和 `spare-parts-service`。控制台显示每个服务各有一个健康实例。
- 网关通过 `lb://work-order-service` 按服务名转发。工单服务的 `SparePartsClient` 通过 Feign 按服务名调用备件服务的 `/internal/parts/ping`。
- Apifox 和终端均验证了 `GET http://localhost:8088/ping/parts` 返回 HTTP 200 和 `SPARE-PARTS-PONG`。终端同时复测了 `GET http://localhost:8088/ping` 返回 HTTP 200 和 `PONG`。

## Java 基础与算法

- 能区分分别运行三个应用产生的三个 JVM 进程，以及工单服务进程内部处理请求的线程。课堂上纠正了 `run()` 和 `start()` 的执行者。直接调用 `run()` 由当前线程执行，调用 `start()` 会启动新线程执行任务。
- 练习了 `NEW`、`TIMED_WAITING`、`BLOCKED` 和 `WAITING`，能判断等待进入 `synchronized` 和无超时 `join()` 分别对应的状态。
- 使用 JDK 17 运行反转单链表练习，确认三节点反转后依次输出 `3`、`2`、`1`，单节点保留原节点，空链表返回 `null`。反转方法遍历一次链表，只使用固定数量的引用，时间复杂度为 `O(n)`，额外空间复杂度为 `O(1)`。 [BlogHub 算法练习](https://github.com/Sebastian061524/BlogHub/blob/ef6d16fef173b324dc6bd3cb98de853b047014cc/algorithm/reverseList.java)

## 下次从这里继续

- 先独立复述线程状态和网关、Nacos、Feign 各自的职责，再开始 Day 9 工单业务。第一步从报修接口入手，随后验证状态流转和跨校区权限。
- Day 9 的 Java 基础是 `synchronized`、锁与线程安全，算法是合并两个有序链表。
