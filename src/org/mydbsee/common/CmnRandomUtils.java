/**
 *
 */
package org.mydbsee.common;

import java.util.HashSet;
import java.util.Random;

/**
 * @author Think
 *
 */
public class CmnRandomUtils {

	
	public static int getRadomInt(int min,int max) {
		return (int) Math.round(Math.random()*(max-min)+min);
	}
	/**
	 * 产生一个指定长度的字符串。
	 */
	public static String createRadomStrs(int length, Boolean isMin,
	boolean number) {
		char[] result = new char[length];
		int m = 0;
		for (; m < length;) {
			int by = createRadomNum(256, true);
			if (number) {
				if (isMin == null) {
					if ((by >= 48 && by <= 57)) {
						result[m] = (char) by;
						m++;
					}
				} else if (isMin) {
				if (by >= 97 && by <= 122 || (by >= 48 && by <= 57)) {
					result[m] = (char) by;
					m++;
				}
			} else {
			if ((by >= 65 && by <= 90) || (by >= 48 && by <= 57)) {

				result[m] = (char) by;

				m++;
			}
			}
			} else {
				if (isMin) {
					if ((by >= 97 && by <= 122) || (by >= 65 && by <= 90)) {
						result[m] = (char) by;
						m++;
					}
				} else if (isMin) {
				if (by >= 97 && by <= 122) {
				result[m] = (char) by;
				m++;
				}
			} else {
			if ((by >= 65 && by <= 90)) {
				result[m] = (char) by;
				m++;
			}
			}

			}
		}
	return String.copyValueOf(result);
	}
	 /**
	 * 随机指定范围内N个不重复的数
	 * 最简单最基本的方法
	 * @param min 指定范围最小值
	 * @param max 指定范围最大值
	 * @param n 随机数个数
	 */
	public static int[] randomCommon(int min, int max, int n){
		if (n > (max - min + 1) || max < min) {
            return null;
        }
		int[] result = new int[n];
		int count = 0;
		while(count < n) {
			int num = (int) (Math.random() * (max - min)) + min;
			boolean flag = true;
			for (int j = 0; j < n; j++) {
				if(num == result[j]){
					flag = false;
					break;
				}
			}
			if(flag){
				result[count] = num;
				count++;
			}
		}
		return result;
	}
	 /**
	 * 随机指定范围内N个不重复的数
	 * 利用HashSet的特征，只能存放不同的值
	 * @param min 指定范围最小值
	 * @param max 指定范围最大值
	 * @param n 随机数个数
	 * @param HashSet<Integer> set 随机数结果集
	 */
    public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min)) + min;
            set.add(num);// 将不同的数存入HashSet中
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
        	randomSet(min, max, n - setSize, set);// 递归
        }
    }
    public static void main(String[] args) {
    	
    	for (int i = 0; i < 100; i++) {
			System.out.println(getRadomInt(1,5));
		}
    	
//		int[] reult1 = randomCommon(20,50,10);
//		for (int i : reult1) {
//			System.out.println(i);
//		}
		
//		int[] reult2 = randomArray(20,50,31);
//		for (int i : reult2) {
//			System.out.println(i);
//		}
//		
//		HashSet<Integer> set = new HashSet<Integer>();
//		randomSet(20,50,10,set);
//        for (int j : set) {
//        	System.out.println(j);
//		}
	}
	 /**
	 * 随机指定范围内N个不重复的数
	 * 在初始化的无重复待选数组中随机产生一个数放入结果中，
	 * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换
	 * 然后从len-2里随机产生下一个随机数，如此类推
	 * @param max  指定范围最大值
	 * @param min  指定范围最小值
	 * @param n  随机数个数
	 * @return int[] 随机数结果集
	 */
	public static int[] randomArray(int min,int max,int n){
		int len = max-min+1;
		
		if(max < min || n > len){
			return null;
		}
		
		//初始化给定范围的待选数组
		int[] source = new int[len];
        for (int i = min; i < min+len; i++){
        	source[i-min] = i;
        }
        
        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
        	//待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
	}
	/**
	 * 产生一个的随机整数
	 */
	public static int createRadomNum(int max, boolean hasZero) {
		if (hasZero) {
			int s = new Double(max * Math.random()).intValue();
			return s;
		} else {
			int s = new Double(max * Math.random()).intValue();
			return s == 0 ? 1 : s;
		}
	}


	/**
	 * 产生一个的随机Double数
	 */
	public static Double createRadomDoubleNum(int max, boolean hasZero) {
		if (hasZero) {
			Double s = new Double(max * Math.random()).doubleValue();
			return s;
		} else {
			Double s = new Double(max * Math.random()).doubleValue();
			return s == 0 ? 1.0 : s;
		}
	}
}
