# dependencyManagement 细节说明
1. Maven 使用  <dependencyManagement> 元素来提供了一种管理依赖版本号的方式，
通常在项目 packaging为POM，中使用  <dependencyManagement> 元素
2. 使用 pom.xml 中的  <dependencyManagement> 标签元素能让所有在子项目中引用一个依赖，
Maven 会沿着父子层次向上走，直到找到一个拥有  <dependencyManagement> 标签元素的项目，
然后它就会使用这个  <dependencyManagement> 元素中指定的版本号。
3.好处: 如果有多个项目都引用同一样依赖，则可以避免在每个使用的子项目里都声明一个版本号，
当升级或切换到另一个版本时，只需要在顶层父容器更新，而不需要分别在子项目的修改；另外
如果某个子项目需要另外一个版本，只需要声明 version 就可以。
4.  <dependencyManagement> 里只是声明依赖，并不实现引入，因此子项目需要显示的声明需要的依赖
5. 如果不在子项目中声明依赖，是不会从父项目中继承下来的；只有在子项目中写了该依赖项，并且
没有指定具体版本，才会从父项目中继承该项，并且version和scope都读取自父 pom
6. 如果子项目中指定了版本号，那么会使用子项目中指定的jar版本

