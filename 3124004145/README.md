# 论文查重

个人编程作业：设计一个论文查重算法，给定原文文件与经过增删改的抄袭版文件，输出二者重复率（浮点型，精确到小数点后两位）。

## 运行环境

- JDK 11+（开发时使用 JDK 21）
- Windows 10 64-bit

## 构建

在项目根目录执行：

```bat
build.bat
```

或手动执行：

```bat
javac -encoding UTF-8 -d build\main src\plagiarism\*.java
jar --create --file main.jar --main-class plagiarism.Main -C build\main .
```

## 运行

```bat
java -jar main.jar <原文文件绝对路径> <抄袭版文件绝对路径> <答案文件绝对路径>
```

例如：

```bat
java -jar main.jar C:\tests\org.txt C:\tests\org_add.txt C:\tests\ans.txt
```

答案文件将写入一个 `0.00` ~ `1.00` 的浮点数，保留两位小数。

## 运行单元测试

```bat
javac -encoding UTF-8 -d build\test -cp build\main test\plagiarism\*.java
java -cp "build\main;build\test" plagiarism.TestRunner
```

## 算法说明

采用**字符 bigram（二元组）词频向量的余弦相似度**：

1. 归一化文本：去除标点与空白，仅保留中英文字母与数字，英文统一转小写；
2. 将文本切分为字符二元组（长度不足 2 的文本退化为单个 unigram）；
3. 统计各 gram 词频，构造词频向量；
4. 计算两向量的余弦相似度，结果天然落在 [0, 1] 区间，直接作为重复率。

## 项目结构

```
src/plagiarism/
  Main.java                       程序入口
  CliArguments.java               命令行参数封装与校验
  PlagiarismChecker.java          查重主流程（读文件 → 算相似度 → 写答案）
  SimilarityCalculator.java       相似度算法接口
  CosineSimilarityCalculator.java 余弦相似度实现
  TextNormalizer.java             归一化与 n-gram 工具
  PlagiarismException.java        异常基类
  InvalidArgumentException.java   参数非法异常
  FileReadException.java          读文件异常
  FileWriteException.java         写文件异常
test/plagiarism/
  TestRunner.java                 轻量单元测试运行器
  SimilarityCalculatorTest.java   算法单元测试
  PlagiarismCheckerTest.java      流程与异常单元测试
```
