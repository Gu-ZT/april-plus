# AprilPlus

AprilPlus 是一个基于 Fabric 的 Minecraft 模组，面向 `26w14a` 快照版本，聚焦于活化方块（Living Block）玩法增强、客户端操作优化与稳定性修复。

## 版本与兼容性

- 当前版本：`0.0.14`
- Minecraft：`26w14a`（`26.1.1-alpha.26.14.a`）
- Fabric Loader：`0.18.6+`
- Fabric API：`0.145.2+26w14a`

## 主要功能

### 客户端增强

- 灵魂出窍（Free Camera）
- 伽马覆写（Gamma Toggle）
- 活化方块分组快速切换（小键盘 `0~6` 与 `.`）

### 交互优化

- 双击快速选中附近相同物品类型的活化方块
- `Shift + 右键` 取消当前选中
- `Shift + 双击右键` 批量取消附近同组选中并清组
- 熔炉不会熔炼被选中的活化方块

### 0.0.14 新增

- 添加按住 `Shift` 的序列操作指令模式
- 按住 `Shift` 下达命令会进入队列（而非立即执行），活化方块空闲时按顺序执行

## 稳定性修复（节选）

- 修复启动崩溃问题（含早期 #2 相关问题）
- 修复 `X + <number>` 崩溃
- 修复合成器相关 `NPE` 与活化方块 tick 相关 `CCE`
- 修复活化方块为空气/物品形态时的异常消失问题（多轮）
- 修复活化方块无法选中的问题

> 详细历史请查看 `CHANGE_LOG.md`。

## 默认按键

- `G`：Gamma Toggle
- `` ` ``（反引号）：Free Camera
- 小键盘 `0`：Group None
- 小键盘 `1`：Group Red
- 小键盘 `2`：Group Blue
- 小键盘 `3`：Group Lime
- 小键盘 `4`：Group Yellow
- 小键盘 `5`：Group Purple
- 小键盘 `6`：Group Aqua
- 小键盘 `.`：Group All
