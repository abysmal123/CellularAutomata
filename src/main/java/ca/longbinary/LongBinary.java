package ca.longbinary;

public class LongBinary {
	
// public:
	public final int len;		// 二进制串长度
	
	public final int[] blocks;	// 二进制串分块
	
	public final int n;			// 分块个数
	
	public LongBinary(int len) {
		
		if (len < 1) {
			throw new IllegalArgumentException("长度至少为1。");
		}
		this.len = len;
		n = len / 32 + (len % 32 == 0 ? 0 : 1);
		blocks = new int[n];
	}
	
	public LongBinary(int len, int[] blocks) {
		
		if (len < 1) {
			throw new IllegalArgumentException("长度至少为1。");
		}
		this.len = len;
		n = len / 32 + (len % 32 == 0 ? 0 : 1);
		if (n != blocks.length) {
			throw new IllegalArgumentException("分块大小与长度不匹配。");
		}
		this.blocks = new int[n];
		for (int i = 0; i < n; i++) {
			this.blocks[i] = blocks[i];
		}
	}
	
	public LongBinary(LongBinary lb) {
		
		len = lb.len;
		n = lb.n;
		this.blocks = new int[n];
		for (int i = 0; i < n; i++) {
			this.blocks[i] = lb.blocks[i];
		}
	}
	
	public LongBinary(String s) {
		
		len = s.length();
		if (len < 1) {
			throw new IllegalArgumentException("长度至少为1。");
		}
		n = len / 32 + (len % 32 == 0 ? 0 : 1);
		this.blocks = new int[n];
		for (int i = 0; i < len - 32 * (n - 1); i++) {
			this.blocks[0] <<= 1;
			this.blocks[0] |= (s.charAt(i) - '0');
		}
		for (int i = len - 32 * (n - 1); i < len; i++) {
			int blockIdx = (i + 32 * n - len) / 32;
			this.blocks[blockIdx] <<= 1;
			this.blocks[blockIdx] |= (s.charAt(i) - '0');
		}
	}
	
	public int getPos(int i) {			// 返回索引i处的值
		
		return (blocks[n - 1 - i / 32] >> (i % 32)) & 1;
	}
	
	public void setPos(int i, int value) {			// 设置索引i处的值为value
		
		value &= 1;
		if (value == 1) {
			blocks[n - 1 - i / 32] |= (1 << (i % 32));
		} else {
			blocks[n - 1 - i / 32] &= ~(1 << (i % 32));
		}
	}
	
	public static LongBinary bAND(LongBinary lb1, LongBinary lb2) {		// 按位与运算
		
		if (lb1.len < lb2.len) {
			LongBinary temp = lb1;
			lb1 = lb2;
			lb2 = temp;
		}
		LongBinary res = new LongBinary(lb1);
		int minN = lb2.n, diff = lb1.n - lb2.n;
		for (int i = minN - 1; i >= 0; i--) {
			res.blocks[i + diff] &= lb2.blocks[i];
		}
		for (int i = diff - 1; i >= 0; i--) {
			res.blocks[i] = 0;
		}
		return res;
	}
	
	public static LongBinary bOR(LongBinary lb1, LongBinary lb2) {		// 按位或运算
		
		if (lb1.len < lb2.len) {
			LongBinary temp = lb1;
			lb1 = lb2;
			lb2 = temp;
		}
		LongBinary res = new LongBinary(lb1);
		int minN = lb2.n, diff = lb1.n - lb2.n;
		for (int i = minN - 1; i >= 0; i--) {
			res.blocks[i + diff] |= lb2.blocks[i];
		}
		return res;
	}
	
	public static LongBinary bXOR(LongBinary lb1, LongBinary lb2) {		// 按位异或运算
		
		if (lb1.len < lb2.len) {
			LongBinary temp = lb1;
			lb1 = lb2;
			lb2 = temp;
		}
		LongBinary res = new LongBinary(lb1);
		int minN = lb2.n, diff = lb1.n - lb2.n;
		for (int i = minN - 1; i >= 0; i--) {
			res.blocks[i + diff] ^= lb2.blocks[i];
		}
		return res;
	}
	
	public static LongBinary bNOT(LongBinary lb) {			// 按位非运算
		
		LongBinary res = new LongBinary(lb);
		int n = lb.n;
		for (int i = 0; i < n; i++) {
			res.blocks[i] = ~res.blocks[i];
		}
		int MASK = 1;
		for (int i = 1; i < lb.len - 32 * (n - 1); i++) {
			MASK <<= 1;
			MASK |= 1;
		}
		res.blocks[0] &= MASK;
		return res;
	}
	
	public static LongBinary SHL(LongBinary lb, int k) {		// 逻辑左移运算
		
		if (k >= lb.len) {
			return new LongBinary(lb.len);
		}
		StringBuffer buffer = new StringBuffer(lb.toString());
		buffer.delete(0, k);
		for (int i = 0; i < k; i++) {
			buffer.append('0');
		}
		return new LongBinary(buffer.toString());
	}
	
	public static LongBinary SHR(LongBinary lb, int k) {		// 逻辑右移运算
		
		if (k >= lb.len) {
			return new LongBinary(lb.len);
		}
		StringBuffer buffer = new StringBuffer(lb.toString());
		buffer.delete(lb.len - k, lb.len);
		for (int i = 0; i < k; i++) {
			buffer.insert(0, '0');
		}
		return new LongBinary(buffer.toString());
	}
	
	public String hashString() {
		
		StringBuffer code = new StringBuffer();
		for (int block : blocks) {
			code.append(block + ".");
		}
		return code.toString();
	}
	
	@Override
	public String toString() {
		
		char[] s = new char[len];
		for (int i = 0; i < len; i++) {
			s[len - 1 - i] = ((blocks[n - 1 - i / 32] >> (i % 32)) & 1) == 1 ? '1' : '0';
		}
		return new String(s);
	}
	
	@Override
	public int hashCode() {
		
		return hashString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o instanceof LongBinary) {
			int len = ((LongBinary)o).len;
			if (len != this.len) {
				return false;
			}
			int[] blocks = ((LongBinary)o).blocks;
			for (int i = 0; i < n; i++) {
				if (blocks[i] != this.blocks[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
