## 赛题

**问题为求解1000位的序列密码的k错线性复杂度，记分方式为$N+k$，其中N为提交的多项式的次数，k为由此多项式生成的序列与原序列的汉明距离，即与原序列的“差”**


## 现有方法

查阅资料后发现的现有方法主要可以求解以下几种问题

- 任意序列的最小多项式（BM算法）
- $2^n$的周期序列的k错线性复杂度(GC)
- 补全至$2^n$周期的k错线性复杂度(SM)
- $p^n$的周期序列的k错线性复杂度
- 另外对k较小时的研究较多

这些方法与赛题还是有一些差距所在，主要有以下几个问题

- 已有方法大多针对周期序列，即需要知道至少一个周期的序列。然而赛题中只是一个有限长序列
- 通过SM算法求得的k错线性复杂度其实还是周期序列的复杂度，相比非周期而言要大一些
- 在本赛题中，可以尝试用较大的错误值来换取整体评分，但是因为没有实际意义，所以相关研究很少

## 尝试1

有一篇论文是用混合遗传算法进行的任意长序列的求解，尝试后发现以下几个问题

- 两个相近的错误序列之间可能导致的最小多项式次数相差很大，上限约为多项式中系数为1的项的个数。从这个角度说，遗传算法近似于随即搜索算法
- 算法开始时需要进行种群初始化。对于本题1000位而言，可选的种群总数有$2^{1000}$，理论上而言，若要选出有足够进化的样本个数也是一个天文数字。

最终结果很不理想，原论文最多也只是对256位长度，错误数不超过8位进行了计算。

## 尝试2

一个序列的极小多项式$1+a_{m}x+\cdots+a_0x^m$相当于满足一个线性方程组，由此，我想能不能计算以下问题：

**给定多项式的次数$m$，求解汉明重量最小的错误序列（记为$\vec e$），原序列（记为$\vec s$），使得$\text{LC}(s+e)\le m$**



此时新的序列$s+e$要满足的同样是一个线性方程组，只不过该方程组的方程个数被限定不超过$m$。
![微信图片_20200520203759](%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200520203759.jpg)

其中$a_m=1$

记为$A\times(\vec s +\vec e)=\vec 0$

等价于$A\times\vec s=A\times\vec e=\vec{p}$



在$A$确定的情况下，退化成为一个线性方程组$A\times\vec e=\vec p$，且一定有解。求解目标是使得 $\vec e$ 的汉明重量最小。

记$A\times\vec e=\vec 0$ 的解空间为$L$，则该方程组的解空间 $\vec s+L$

要寻找的就是$\vec s+L_e$ 中离原点最近的一个向量，即$\vec  s$ 到该$L$ 的距$H(A)$



当$A$只有次数给定的情况下，就是在求$H(A)$ 的在限定$A $ 的取值后的最小值。

从这个思路出发，可以通过初始化$A$ 之后再求导的方式来求得次优解。



设$\vec i=\begin{bmatrix}1\\1\\1\\\vdots\\1 \end{bmatrix}$， 解空间$L$ 的基矩阵为$C$ ，则
$$
H(A)=\vec i^T(I-C(C^TC)^{-1}C^T)\vec s\tag{1}
$$


观察$C$，其可以写成如下形式

![微信图片_20200520215424](pic/%E8%B5%9B%E9%A2%98/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200520215424.jpg)

则
$$
C^TC=
\begin{bmatrix}
I&E^T
\end{bmatrix}
\begin{bmatrix}
I\\E
\end{bmatrix}
=I+E^TE\tag{2}
$$
将$C^TC$ 记为$P$
$$
CP^{-1}C^T=
\begin{bmatrix}
I\\E^T
\end{bmatrix}
P
\begin{bmatrix}
I&E
\end{bmatrix}
=
\begin{bmatrix}
P^{-1}&P^{-1}E^T\\
EP^{-1}&EP^{-1}E^T
\end{bmatrix}\tag{3}
$$
综合$(1),(2),(3)$可得
$$
H(A)=\vec i^T
\begin{bmatrix}
I-P^{-1}&P^{-1}E^T\\
EP^{-1}&I-EP^{-1}E^T
\end{bmatrix}
\vec s
\tag{4}
$$
接下来考察$H(A)$ 的偏导数，以$a_0$为例
$$
\frac{\part H(A)}{\part a_0}=\vec i^T
\begin{bmatrix}
\displaystyle-\frac{\part P^{-1}}{\part a_0}&\displaystyle\frac{\part P^{-1}E^T}{\part a_0}\\
\displaystyle\frac{\part EP^{-1}}{\part a_0}&\displaystyle\frac{\part EP^{-1E^T}}{\part a_0}
\end{bmatrix}
\vec s
\tag{5}
$$
其中有
$$
\frac{\part P^{-1}}{\part a_0}=-P^{-1}\frac{\part P}{\part a_0}P\tag{6}\\
$$

$$
\frac{\part P}{\part a_0}=\frac{\part (I+EE^T)}{\part a_0}=\frac{\part E}{\part a_0}E^T+E\frac{\part E^T}{\part a_0}\tag{7}
$$
对于$\displaystyle\frac{\part E}{\part a_0}$，$C$ 中的每一个列向量$\vec l_i=\begin{bmatrix} l_{i1}\\l_{i2}\\l_{i3}\\\vdots\\l_{in} \end{bmatrix}$都有 $A\times \vec l_i=\vec 0$ ，因而有递推公式
$$
l_{ij}=a_{m-1}l_{i(j-1)}+a_{m-2}l_{i(j-2)}+\cdots+a_{0}l_{i(j-m)}\tag{8}\\

$$

$$
\frac{\part l_{ij}}{\part a_0}=l_{i(j-m)}\tag{9}
$$



同时因为$\vec l_i$ 前$m-1$ 项选取了特殊值，因此有

![微信图片_20200520224944](pic/%E8%B5%9B%E9%A2%98/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200520224944.jpg)
$$
\tag{10}
$$


将其记为$I'_0$ ，综合$(5)(6)(7)(8)(9)(10)$ 可得
$$
\frac{\part H(A)}{\part a_0}=\vec i^T
\begin{bmatrix}
-Q&QE^T+I_0'^TP^{-1}\\
\\
EQ+I_0'P^{-1}&-I_0'P^{-1}E^T-EP^{-1}I_0'-EQE^T
\end{bmatrix}
\vec s
\tag{11}
$$
其中
$$
Q=\frac{\part P^{-1}}{\part a_0}=-P^{-1}(I_0'^TE+E^TI_0')P
$$


依照$(11)$ 式可以得出$\left( \displaystyle\frac{\part H(A)}{\part a_0},\displaystyle\frac{\part H(A)}{\part a_1},\displaystyle\frac{\part H(A)}{\part a_2},\cdots,\displaystyle\frac{\part H(A)}{\part a_{m-1}} \right)$ ，作为多项式的调整方向。



**重要：本方法还未得到验证**

---

#### 一些改进方法

- 求导可以进行多次
- 初始化$A$ 时可以多选几组值
- 可以加入一些随机因素来跳出局部最优
