@Author XingTian
@Version v1.1.4
@Since 2023-9-26
@Update 2024-8-16

推荐使用版本
xingtool v1.0.1, v1.0.4, v1.0.5
xingtools v1.1.3, v1.1.4, v1.1.5, v1.1.6
极力推荐使用最新版 v1.1.7


更新公告
2025-5-6 v1.1.7
更改部分
1.移除 JavaVersion.EIGHT 防止报错
2.状态节点 StatusNode 面向接口 Collection
3.XTDateUtil 更新

新增部分
1.新增 EmptyChecker 等系列接口
2.新增 IPageDetail 接口，及其系列实现接口
3.新增链式调用接口 TaskChain, TaskNode 及实现类
4.新增IValueSource 接口, 拥有setValue方法

2024-10-18 v1.1.6
更改部分
1.*重构迭代器，比较器，使之更通用，更健壮
2.提取 XTMath 的二分查找 为 XTBinarySearch, 支持多种二分查找，类似 c++ lower_bound, upper_bound

新增部分
1.XTCallable 新增 getInCatchRet 更加健全
2.*继承重构1.0.5 版本的 XTEncryptUtil 加密工具类，支持Md5,SHA256,DES,AES,RSA 算法
3.新增IdNode接口 (含 getId 方法),新增DataNode作为IDataValue 默认实现类
4.新增编码/哈希/加密算法接口和多个实现类，CryptHandler 是所有算法的基类
5.新增 ECC 加密，XTEncryptUtil 改名为 XTCryptUtil , 使综合工具更明确
6.* 新增 BTree (B 树) 和 BPlusTree (B+ 树) 数据结构，支持插入，删除，查找，遍历等操作，使用新的迭代器速度更快
7.新增 MapEntry 实体类兼容 Map.Entry 接口和其他实体类
8.*新增对 spring-security-oauth2-authorization-server 的 OAuth2 的管理接口，以及各种实现类，后续也会持续跟进更新



2024-9-27 ~ 2024-9-29   v1.1.5
更改部分
1.* 重构 IResult 实现类四大类，更改里面的所有 success 和 error 的默认值,  true 和 false 统一改为 null , 不带值
2.* 重构 ResultUtil 的 大部分方法，为适应不同情况，可以自定义策略等
3.* 更改并提取 XTCallable 使其更通用，更健壮。并更改部分方法名，并应用其他例如 XTAsync 实现更改。
4.大部分类将升级 e.printStackTrace() 这种异常输出形式, 大部分类将引入 异常处理消费者，来控制开关，未来将系统处理异常或改为抛出

新增部分
1.新增 XTSupplier 作为 XTCallable 的补充，使其更灵活，扩展性好。从XTCallable 提出公共静态方法 XTAround , 很多方法优先使用该类
2.新增接口 IName, IRName ,提供getName 方法, 并使Constants 和 HttpStatus 实现该接口, IResult 四大实现类支持配置msg默认英文名name


2024-8-16 ~ 2024-9-14  v1.1.4

更改部分
1.恢复 mybatis-plus 插件自动配置, 默认关闭 xt-lock-aop
2.日志包ILogProvider从静态改为面向对象
3.WebHandler 接口 参数 HttpServletRequestAdapter 变更为 HttpServletRequestDataAdapter
4.*为适应不同需求，ResultUtil新增select方法返回封装对象
5.*对锁的类进行解耦操作, 修改LockHandler, XTLockHandler 等类, 修改LockHelper接口, LockHandler 移植到 core 包
6.针对 Redis 的 AccessLimitUtil 工具类进行更改, 将使用 LimitStrategy 策略; 更改 RedisLimitStrategy 可保证原子性
7.对 RequestLimit 系列部分方法进行了修改, 新增 XTRedisScript 组合获取lua限流脚本

新增部分
1.新增多个节点接口，节点状态机 NodeStateMachine ，适配于无边权无事件情况
2.新增Entry类代替hutool的Pair类，更改新增Entry, Pair, TupleEntry, TuplePair 等适应不同情况的二元组, 部分类开始继承Entry而不是hutool的Pair
3.*新增链式接口，过滤器链接口及其实现类，核心类为 FilterChain (CompositeFilterChainFactory) , StreamChain , BuilderChain , BuilderMapChain可分别模拟过滤器链Filter, 流式Stream的调用方式 和 建造者链, 建造者扩展链, 加快开发
4.*新添 ModuleAdapter 及其实现类，针对 JacksonSerializer 进行了增强，支持更多类似 FastJson 功能，并提供配置全局序列化器，RYRedisCache 支持从容器获取序列化器，容器bean名称分别为 jacksonSerializer 和 redisJacksonSerializer


2024-8-12 v1.1.3
bug 修复
1.由于源码包只识别签名，所以在支持 jdk17 的众多方法会抛出 NoSuchMethodError 错误，故 jdk17 版本不推荐使用 v1.1.2 版本，现对该版本进行紧急修复。

更改部分
1.部分方法参数从 HttpServletRequestData 实体类更改为 HttpServletRequestAdapter接口

新增部分
1.新增HttpServletRequestAdapter和 HttpServletResponseAdapter 针对 HttpServlet 系列的适配方法，并替换之前的长链调用，以便通过对包的支持来达到对jdk支持的目的。
2.新增 HttpServletRequestDataAdapter 和 HttpServletResponseDataAdapter 作为上面两个接口的实现，通过导入不同 jdk 包或者自行实现的方式 满足工具方法的需求。



2024-8-8  v1.1.2
bug 修复
1.由于未导入 spring-boot-starter-aop ，故启用 @EnableXingToolsServer 时使用 某个 aop 报错，目前将所有 aop 关闭；现在不使用 aop 不导入 aop 包，依旧能运行服务。
在v1.1.1版本需要配置  xingtools.ext-transaction-anno.enabled=false # v1.1.1 版本需要关闭。
2.修复 IntStatus 等参数校验注解未初始化的问题，以及数字匹配 matchNum 绝对匹配放行的问题。


更改部分
1.将 pkg-jdk8 和 pkg-jdk17 部分代码移除或移出到mvc包和cloud等其他包下，只做javax 和 jakarta 包的兼容，该包只依赖 core包。依赖关系更新。
2.cloud包将支持 cloud 和 security 两种模块，可以根据需要按需导入模块。

新增部分
1.cloud 包新增对 spring-security , spring-security-oauth2 和 spring-authorization-server 等不同依赖的支持。并提取两个依赖中的公共元素合并作为新的类集合。
例如：AuthToken, AuthAccessToken, AuthTokenExtractor, AuthTokenGenerator 等作为新的框架，和新的 XTAuthenticationBuilder 工具建造类对授权执行链的支持。
2.新增 HttpStatus 作为 Constants 的另一种实现形式。并对 Result 等一系列返回封装类添加对应方法。
3.新增系列集合类对 short 和 float 的支持，并新增 @ShortStatus 和 @FloatStatus 等参数校验注解。


2024-7-10 ~ 2024-7-17  v1.1.1
中版本更新，为了保证灵活性。更改工具 xingtool -> xingtools 。
更改部分
1.分离为多个包，例如分离core 包和 log 大包，保证 log 大包能够使用
2.部分强耦合方法被移除，例如移出 XTArrayUtil 的 logPrintln 等方法
3.更改大部分包名，例如 designtools.http 包名-> designtools.convert
4.部分类按包名合并，例如 designtools.distributed 合并到 utils.ee 里面
5.部分类消失简化操作等，例如Security Oauth的装配

移除部分
1.多个历史遗留类移除，例如移除类XTDataType
2.很多业务类和重复工具类移除，例如暂时移除 ruoyi 和 ican 的多个类，后续会合并到大工具类。如果已经使用了这些类的方法，请留级到 xingtool的 v1.0.5 版本
3.移除其他暂时不需要的类和冗余类，例如kotlin依赖等

支持 jdk8, jdk17 双版本
(为保证移植性，javax包减少使用，无法去掉则移植到 pkg-jdk8 包)
大包分为许多小包，可以按需依赖。
后续版本将 pkg 包降低其对 其他包依赖性，提高扩展性。



2024-4-8 ~ 2024-7-7  v1.0.5
更改部分
1.允许 OptData 直接接受 null 值
2. RedisLockUtil 更改包名为 redis

新增部分
1.添加 Method Proxy 系列方法
2.新增 Serializer 序列化接口，及其子类 多种序列化类
3.新增 model 包，模型含有状态机等多种结构
4.扩展 OptBundle 系列方法

支持部分 SpringBoot3
新增使用文档

2023-12-29 ~ 2024-3-27  v1.0.4

bug修复
1.紧急修复XTCallable 的 getSupplier 和 canRunTask 问题，并修复逻辑。
2.紧急修复 XTStrUtil findFirstOf错误调用自身的问题。 1.0.2-1.0.3 两个版本不要使用该方法。
3.修复单 pick 问题, 解决并发注解 @MainThread @SonThread 事务问题, 修复 TreeUtil 树转列表的问题
4.修复 ResultUtil 对 R类 的支持问题

更改部分
1.修改 @MainThread 默认时间策略为  GetResultAfterLastSon, 和原来没有什么差别。
2.修改了 XTMethodUtil 类方法名称 isAnnotationPresent => isHandlerMethodAnnotationPresent。
3.修改所有限流注解/工具的位置。
4.修改 XTLog 实现, 以及新增一系列 web 的 key 接口 和日志接口。
5.修改XTThreadPool、ThreadData默认核心线程，并修改ThreadHelper 命名防止冲突。

新增部分
1.新增 @StrJson 注解 返回数据时返回指定json字符串，并新增 StrJsonStrategy 接口的实现类 SensitiveSerializer 类用于敏感字符串脱敏，可自行实现StrJsonStrategy接口。
2.新增 SocketServer 和 SocketClient 等类 和 XTCollUtil 集合工具类。
3.新增 @RequestLimit 限流注解 和 RequestLimitHandler 工具类，可以取代 @Limit 和 @AccessLimit 注解,  提供了两个策略，可自定义策略，使用灵活。
4.新增 @WebLog注解 和 WebHandler 工具类，用于自定义策略日志打印，可以取代 @MethodLog 和 @XTSystemLog , 匹配, 模式串用法更灵活。
5.新增 OptBundle 类 用于执行链操作 , 和 OptionalResult 用于扩展 Optional 类。
6.新增 kotlin 依赖 临时支持 kotlin 。



2023-10-24 ~ 12-25  v1.0.3

bug修复
1.修复了 AccessLimitUtil.limitIP 加载 Ipdb 错误 的bug
更改部分
1.修改了XTCompletionService实现，以及ThreadLocalHelper文件位置
2.所有web 拦截类 从 Result 改为返回 IResult 接口，并且添加 GlobalResultFactory 接口用于全局返回结果，使用时需要注册到容器。
新增部分
1.新增top.cutexingluo.tools.utils.se.algo.cpp包，里面包含各种算法（有些未测试），例如数论，几何，数据结构，图论，字符串等
2.新增 BoolUtil  用于使 java 适配 c++性质。
3.新增XTArrayUtil一些方法，用于移动数组元素，新增 XTSetUtil 的 Set 工具类。
4.新增ClassMaker类，用于转化和反射，可以配合XTObjUtil。
5.增加了 CommonResult 类，用于返回通用结果, 四大返回类重新继承了该类。
6.新增红黑树 RBTree，迭代器默认中序遍历，即默认升序排序。属性全为protected，方便子类继承。常规推荐使用 TreeMap
7.新增各种迭代器用于适配多种情况。可自行继承使用。
8.新增启用 server 的 banner 和 cloud server 的 banner



2023-10-1 ~ 10-16  v1.0.2
1.添加了Supplier接口
2.添加多线程注解AOP @MainThread @SonThread
3. 调整 XTAsync 类 , 并且添加 ThreadHelper接口，更快速使用。
4. 添加 XTString 工具类，可以通过 C++ 方法名称的方式使用。
5.重构代码（位置和代码），减小 Bean 数量，以下为 v1.0.2 重构更新日志：

1.移除 XTExceptionAop 两个静态方法
2.添加 BaseAspectAroundHandler 接口默认方法
3.规范类的命名，例如ThreadLocal的工具类，规范了各工具类的用法注释
4.更改了 XTCallable 和 XTRunnable 的部分方法，添加 TryCatchHelper 等helper接口，方便直接使用工具类。例如 LockHelper, ThreadHelper 等
5.更改 XTProxy 的实现
6.更改了 XTResponseUtil 的参数, 使之更通用
7.新增 TreeUtil  树转列表
8.更改 LogInfo 类 转为 LogInfoDisable 类
9.为 RedisConfig , SpringSecurity Oauth2 添加用法注释
10.XTCallOtherUtil 更名为 XTCodeInteropUtil，关于生成其他语言代码的工具类
11.添加RabbitMQ系列初始工具类, 以及用法Test类, 可以不使用，没有太多优化的地方。

2023-9-26 v1.0.1
正式版发布，中央仓库 ,  依赖最低版本不能低于 v1.0.1

2023-9-26 v1.0.0
测试仓库版

2023-4-6 beta - v1.2.1
整理重构代码，整理返回类。

2023-4-6 beta - v1.2.0
整理重构代码，依赖处理。

2023-2-3 beta - v1.1.0
整合工具类为springboot包
取消XTUtils类，JUC和锁 默认包过时

2022-11-26 beta - v1.0.3
增加JUC集合,本地方法类

2022-11-14 beta - v1.0.2
增加aop注解，XTFile工具类

2022-10-19 beta - v1.0.1
更新设计工具等工具类

2022-10-18 beta - v1.0.0
默认XTUtils工具和异常配置类