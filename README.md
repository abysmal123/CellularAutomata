# CellularAutomata
Java laboratory(maven project) with respect to cellular automata.
# Java classes
 ```java
src/main/java/ca
                ╰━━━━eden   // 无限配置满射问题
                │       ╰━━━━BuildTree.java                             // 由特定规则集作为根节点构造一棵树(ECA)   
                │       ╰━━━━GlobalSurjectivity.java                    // ECA无限配置满射问题 
                │       ╰━━━━GlobalSurjectivityDiameter4.java           // 规则直径4无限配置满射问题 
                │       ╰━━━━ShowProcedureTree.java                     // ECA二叉树的可视化 
                │       ╰━━━━ShowProcedureTreeDiameter4.java            // 规则直径4二叉树的可视化
                │       ╰━━━━TreeNode.java                              // (protected)树节点数据结构
                ╰━━━━finiteconfig   // 有限配置满射问题(王老师论文)
                │       ╰━━━━FiniteConfigD4.java                        // ECA有限配置满射问题
                │       ╰━━━━FiniteConfigECA.java                       // 规则直径4有限配置满射问题
                ╰━━━━fixedboundary    // 反射边界满射问题
                │       ╰━━━━FixedD4L1FiniteLength.java                 // 规则直径4（左1右2）固定边界满射问题
                │       ╰━━━━FixedD4L2FiniteLength.java                 // 规则直径4（左2右1）固定边界满射问题
                │       ╰━━━━FixedD5FiniteLength.java                   // 规则直径5固定边界满射问题
                │       ╰━━━━FixedECAFiniteLength.java                  // ECA固定边界满射问题
                │       ╰━━━━LinearSet.java                             // 线性规则集
                │       ╰━━━━ShowProcedureTreeD4L1.java                 // 规则直径4（左1右2）二叉树的可视化 
                │       ╰━━━━ShowProcedureTreeD4L2.java                 // 规则直径4（左2右1）二叉树的可视化 
                │       ╰━━━━ShowProcedureTreeD5.java                   // 规则直径5二叉树的可视化 
                │       ╰━━━━ShowProcedureTreeECA.java                  // ECA二叉树的可视化
                ╰━━━━periodic   // 循环边界满射问题
                │       ╰━━━━PeriodicD4FiniteLength.java                // 规则直径4循环边界满射问题
                │       ╰━━━━PeriodicD5FiniteLength.java                // 规则直径5循环边界满射问题
                │       ╰━━━━PeriodicECAFiniteLength.java               // ECA循环边界满射问题
                │       ╰━━━━ShowProcedureTreeD4.java                   // 规则直径4二叉树的可视化 
                │       ╰━━━━ShowProcedureTreeECA.java                  // ECA二叉树的可视化
                │       ╰━━━━ValueSet.java                              // (protected)堆节点数据结构
                ╰━━━━reflective    // 反射边界满射问题
                │       ╰━━━━ReflectiveD4L1FiniteLength.java            // 规则直径4（左1右2）反射边界满射问题
                │       ╰━━━━ReflectiveD4L2FiniteLength.java            // 规则直径4（左2右1）反射边界满射问题
                │       ╰━━━━ReflectiveD5FiniteLength.java              // 规则直径5反射边界满射问题
                │       ╰━━━━ReflectiveECAFiniteLength.java             // ECA反射边界满射问题
                │       ╰━━━━ShowProcedureTreeD4L1.java                 // 规则直径4（左1右2）二叉树的可视化 
                │       ╰━━━━ShowProcedureTreeD4L2.java                 // 规则直径4（左2右1）二叉树的可视化 
                │       ╰━━━━ShowProcedureTreeD5.java                   // 规则直径5二叉树的可视化 
                │       ╰━━━━ShowProcedureTreeECA.java                  // ECA二叉树的可视化
                ╰━━━━specialinjectivity   // 单射模式构造脚本
                │       ╰━━━━SpecialInjectivity.java                    // 单射模式构造脚本
                ╰━━━━zeroboundary   // 零边界满射问题
                │       ╰━━━━TreeNode.java                              // (protected)树节点数据结构
                │       ╰━━━━ZeroBoundaryD4FiniteLength.java            // 规则直径4零边界满射问题
                │       ╰━━━━ZeroBoundaryD5FiniteLength.java            // 规则直径5零边界满射问题
                │       ╰━━━━ZeroBoundaryDiameter5.java                 // 规则直径5零边界满射问题（针对给定长度配置）
                │       ╰━━━━ZeroBoundaryECA.java                       // ECA零边界满射问题（针对给定长度配置）
                │       ╰━━━━ZeroBoundaryECAFiniteLength.java           // ECA零边界满射问题
                ╰━━━━zeroboundary2  // 零边界满射问题（堆实现）
                │       ╰━━━━ZeroD4L1FiniteLength.java                 // 规则直径4（左1右2）零边界满射问题
                │       ╰━━━━ZeroD4L2FiniteLength.java                 // 规则直径4（左2右1）零边界满射问题
                │       ╰━━━━ZeroD5FiniteLength.java                   // 规则直径5零边界满射问题
                │       ╰━━━━ZeroECAFiniteLength.java                  // ECA零边界满射问题
                │       ╰━━━━ShowProcedureTreeD4L1.java                 // 规则直径4（左1右2）二叉树的可视化 
                │       ╰━━━━ShowProcedureTreeD4L2.java                 // 规则直径4（左2右1）二叉树的可视化 
                │       ╰━━━━ShowProcedureTreeD5.java                   // 规则直径5二叉树的可视化 
                │       ╰━━━━ShowProcedureTreeECA.java                  // ECA二叉树的可视化
                ╰━━━━injectivity    // 无限配置单射问题（Amoroso算法）
                │       ╰━━━━BoxD4.java                                 // (protected)规则直径4：Box数据结构
                │       ╰━━━━BoxD5.java                                 // (protected)规则直径5：Box数据结构
                │       ╰━━━━BoxDn.java                                 // (protected)规则直径n：Box数据结构
                │       ╰━━━━BoxECA.java                                // (protected)ECA：Box数据结构
                │       ╰━━━━GlobalInjectivityD4.java                   // 规则直径4无限配置单射问题
                │       ╰━━━━GlobalInjectivityD5.java                   // 规则直径5无限配置单射问题
                │       ╰━━━━GlobalInjectivityDn.java                   // 规则直径n无限配置单射问题
                │       ╰━━━━GlobalInjectivityECA.java                  // ECA无限配置单射问题
                ╰━━━━injectivity2    // 无限配置单射问题（新算法）
                │       ╰━━━━GlobalInjectivityD4.java                   // 规则直径4无限配置单射问题
                │       ╰━━━━GlobalInjectivityD5.java                   // 规则直径5无限配置单射问题
                │       ╰━━━━GlobalInjectivityDn.java                   // 规则直径n无限配置单射问题
                │       ╰━━━━GlobalInjectivityECA.java                  // ECA无限配置单射问题
                │       ╰━━━━ValueSet.java                              // (protected)堆节点数据结构
                ╰━━━━injectivity3    // 无限配置单射问题（Amoroso算法、哈希表优化）
                │       ╰━━━━BoxDn.java                                 // (protected)规则直径n：Box数据结构
                │       ╰━━━━GlobalInjectivityDn.java                   // 规则直径n无限配置单射问题
