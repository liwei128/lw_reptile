package com.abner.pojo.mi;

import com.abner.utils.JsonUtil;

/**
 * 购物选项
 * @author liwei
 * @date: 2018年6月13日 下午12:54:03
 *
 */
public class Option {
	
	//第几个选项
	private int index;
	
	//选项的值
	private int value;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}

	public Option(int index, int value) {
		super();
		this.index = index;
		this.value = value;
	}

	public Option() {
		super();
	}
	
	
	

}
