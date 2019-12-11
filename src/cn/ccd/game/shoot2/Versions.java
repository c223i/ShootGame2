package cn.ccd.game.shoot2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public interface Versions {
	public static  String NUMBLE ="第2次修改";// 修改次数

	public static final String PROJECT = "ShootGame2";// 项目名
	public static final String EDITION = "Beta";// 版本
	public static final String VERSIONS = "1.0.2";// 版本号 <<<<<< 改这里
	public static final String AUTHOR = "Laxworld";// 作者
	public static final String COPYRIGHT = "CopyRight (C) 2019";// 版权标志

	public static final String SOURCE_CODE = "ShootSourceCode";// 源码名
	public static final String SOURCE_CODE_VERSIONS = "2.0.80";// 源码版本

	public static final String CONTENT = "	";//这里是添加的内容

	// 增了加一个修改的信息的保存方法
	public static void main(String[] args) {
		saveInformation();
	}

	public static void saveInformation() {
		try {

			FileOutputStream fos = new FileOutputStream("./editon.txt", true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			PrintWriter pw = new PrintWriter(bw, true);
			String str = "\tNUMBLE:"+ NUMBLE+ "。\r\n" + 
					"\tPROJECT:" 	+ PROJECT + "。\r\n" + 
					"\tEDITION:"	+ EDITION + "。\r\n" + 
					"\tVERSIONS:"	+ VERSIONS + "。\r\n" + 
					"\tAUTHOR:" 	+ AUTHOR + "。\r\n"+ 
					"\tCOPYRIGHT:" 	+ COPYRIGHT + "。\r\n" + 
					"\tSOURCE_CODE:" + SOURCE_CODE + "。\r\n"+
					"\tSOURCE_CODE_VERSIONS:" + SOURCE_CODE_VERSIONS + "。\r\n" + 
					"\tCONTENT:\r\n\t\t" + CONTENT+ "。\r\n\r\n";
					
			FileInputStream fis = new FileInputStream("./editon.txt");
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			StringBuilder sb=new StringBuilder();
			String b=null;
			while((b=br.readLine())!=null) {
				sb.append(b);
			}
			b=sb.toString();
			if (!(b.contains(NUMBLE))) {
				pw.println(str);
				System.out.println("创建完成");
			}else {
				System.out.println("已经创建过了");
			}
			pw.close();
		} catch (FileNotFoundException e) {
			System.out.println("文件没有找到");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("输入有误");
			e.printStackTrace();
		}

	}

}
