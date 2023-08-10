package kr.co.helf.vo;

import org.apache.ibatis.type.Alias;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Alias("membershipJoinCat")
public class MembershipJoinCategory {

	private int no;
	private String name;
	private int price;
	private int catNo;
	private String catName;
	private String catProperty;
	private String useOption;
}
