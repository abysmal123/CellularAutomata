# CellularAutomata
Java laboratory(maven project) with respect to cellular automata.
# Java classes
 ```java
src/main/java/ca
                �t��������eden   // ����������������
                ��       �t��������BuildTree.java                             // ���ض�������Ϊ���ڵ㹹��һ����(ECA)   
                ��       �t��������GlobalSurjectivity.java                    // ECA���������������� 
                ��       �t��������GlobalSurjectivityDiameter4.java           // ����ֱ��4���������������� 
                ��       �t��������ShowProcedureTree.java                     // ECA�������Ŀ��ӻ� 
                ��       �t��������ShowProcedureTreeDiameter4.java            // ����ֱ��4�������Ŀ��ӻ�
                ��       �t��������TreeNode.java                              // (protected)���ڵ����ݽṹ
                �t��������finiteconfig   // ����������������(����ʦ����)
                ��       �t��������FiniteConfigD4.java                        // ECA����������������
                ��       �t��������FiniteConfigECA.java                       // ����ֱ��4����������������
                �t��������fixedboundary    // ����߽���������
                ��       �t��������FixedD4L1FiniteLength.java                 // ����ֱ��4����1��2���̶��߽���������
                ��       �t��������FixedD4L2FiniteLength.java                 // ����ֱ��4����2��1���̶��߽���������
                ��       �t��������FixedD5FiniteLength.java                   // ����ֱ��5�̶��߽���������
                ��       �t��������FixedECAFiniteLength.java                  // ECA�̶��߽���������
                ��       �t��������LinearSet.java                             // ���Թ���
                ��       �t��������ShowProcedureTreeD4L1.java                 // ����ֱ��4����1��2���������Ŀ��ӻ� 
                ��       �t��������ShowProcedureTreeD4L2.java                 // ����ֱ��4����2��1���������Ŀ��ӻ� 
                ��       �t��������ShowProcedureTreeD5.java                   // ����ֱ��5�������Ŀ��ӻ� 
                ��       �t��������ShowProcedureTreeECA.java                  // ECA�������Ŀ��ӻ�
                �t��������periodic   // ѭ���߽���������
                ��       �t��������PeriodicD4FiniteLength.java                // ����ֱ��4ѭ���߽���������
                ��       �t��������PeriodicD5FiniteLength.java                // ����ֱ��5ѭ���߽���������
                ��       �t��������PeriodicECAFiniteLength.java               // ECAѭ���߽���������
                ��       �t��������ShowProcedureTreeD4.java                   // ����ֱ��4�������Ŀ��ӻ� 
                ��       �t��������ShowProcedureTreeECA.java                  // ECA�������Ŀ��ӻ�
                ��       �t��������ValueSet.java                              // (protected)�ѽڵ����ݽṹ
                �t��������reflective    // ����߽���������
                ��       �t��������ReflectiveD4L1FiniteLength.java            // ����ֱ��4����1��2������߽���������
                ��       �t��������ReflectiveD4L2FiniteLength.java            // ����ֱ��4����2��1������߽���������
                ��       �t��������ReflectiveD5FiniteLength.java              // ����ֱ��5����߽���������
                ��       �t��������ReflectiveECAFiniteLength.java             // ECA����߽���������
                ��       �t��������ShowProcedureTreeD4L1.java                 // ����ֱ��4����1��2���������Ŀ��ӻ� 
                ��       �t��������ShowProcedureTreeD4L2.java                 // ����ֱ��4����2��1���������Ŀ��ӻ� 
                ��       �t��������ShowProcedureTreeD5.java                   // ����ֱ��5�������Ŀ��ӻ� 
                ��       �t��������ShowProcedureTreeECA.java                  // ECA�������Ŀ��ӻ�
                �t��������specialinjectivity   // ����ģʽ����ű�
                ��       �t��������SpecialInjectivity.java                    // ����ģʽ����ű�
                �t��������zeroboundary   // ��߽���������
                ��       �t��������TreeNode.java                              // (protected)���ڵ����ݽṹ
                ��       �t��������ZeroBoundaryD4FiniteLength.java            // ����ֱ��4��߽���������
                ��       �t��������ZeroBoundaryD5FiniteLength.java            // ����ֱ��5��߽���������
                ��       �t��������ZeroBoundaryDiameter5.java                 // ����ֱ��5��߽��������⣨��Ը����������ã�
                ��       �t��������ZeroBoundaryECA.java                       // ECA��߽��������⣨��Ը����������ã�
                ��       �t��������ZeroBoundaryECAFiniteLength.java           // ECA��߽���������
                �t��������zeroboundary2  // ��߽��������⣨��ʵ�֣�
                ��       �t��������ZeroD4L1FiniteLength.java                 // ����ֱ��4����1��2����߽���������
                ��       �t��������ZeroD4L2FiniteLength.java                 // ����ֱ��4����2��1����߽���������
                ��       �t��������ZeroD5FiniteLength.java                   // ����ֱ��5��߽���������
                ��       �t��������ZeroECAFiniteLength.java                  // ECA��߽���������
                ��       �t��������ShowProcedureTreeD4L1.java                 // ����ֱ��4����1��2���������Ŀ��ӻ� 
                ��       �t��������ShowProcedureTreeD4L2.java                 // ����ֱ��4����2��1���������Ŀ��ӻ� 
                ��       �t��������ShowProcedureTreeD5.java                   // ����ֱ��5�������Ŀ��ӻ� 
                ��       �t��������ShowProcedureTreeECA.java                  // ECA�������Ŀ��ӻ�
                �t��������injectivity    // �������õ������⣨Amoroso�㷨��
                ��       �t��������BoxD4.java                                 // (protected)����ֱ��4��Box���ݽṹ
                ��       �t��������BoxD5.java                                 // (protected)����ֱ��5��Box���ݽṹ
                ��       �t��������BoxDn.java                                 // (protected)����ֱ��n��Box���ݽṹ
                ��       �t��������BoxECA.java                                // (protected)ECA��Box���ݽṹ
                ��       �t��������GlobalInjectivityD4.java                   // ����ֱ��4�������õ�������
                ��       �t��������GlobalInjectivityD5.java                   // ����ֱ��5�������õ�������
                ��       �t��������GlobalInjectivityDn.java                   // ����ֱ��n�������õ�������
                ��       �t��������GlobalInjectivityECA.java                  // ECA�������õ�������
                �t��������injectivity2    // �������õ������⣨���㷨��
                ��       �t��������GlobalInjectivityD4.java                   // ����ֱ��4�������õ�������
                ��       �t��������GlobalInjectivityD5.java                   // ����ֱ��5�������õ�������
                ��       �t��������GlobalInjectivityDn.java                   // ����ֱ��n�������õ�������
                ��       �t��������GlobalInjectivityECA.java                  // ECA�������õ�������
                ��       �t��������ValueSet.java                              // (protected)�ѽڵ����ݽṹ
                �t��������injectivity3    // �������õ������⣨Amoroso�㷨����ϣ���Ż���
                ��       �t��������BoxDn.java                                 // (protected)����ֱ��n��Box���ݽṹ
                ��       �t��������GlobalInjectivityDn.java                   // ����ֱ��n�������õ�������
