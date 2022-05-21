# Hilt 注解
1、@HiltAndroidApp ：触发Hilt的代码生成器 作用与 Application类上
2、@AndroidEntryPoint : 创建一个依赖器容器，该容器遵循Android类的生命周期
3、@Module : 告诉 Hilt 如何提供不同类型的实例
4、@InstallIn : InstallIn 用来告诉 Hilt 这个模块会被安装到那个组件上
5、@Provides : 告诉 Hilt 如何获取实例
6、@Singleton : 单例
7、@ViewModelInject : 通过构造函数，给 viewModel 注入实例