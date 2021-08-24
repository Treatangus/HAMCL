package com.zuowu.utils;
import com.launcher.hamcl.user.UserListBean;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * by gsjtceoxl
 */
public class UserDataBaseBox {
    private File rootDic;
	private List<String> userNames = new ArrayList<String>();
    private static UserDataBaseBox Instance;
	private String baseData = "name::@UserName&type::@UserType&password::@Password";
	
	public static UserDataBaseBox getInstance(){
		if(Instance ==null){
			Instance = new UserDataBaseBox();
		}
		return Instance;
	}
	
    private void checkFiles(String mPath){
		rootDic = new File(mPath);
		if(!rootDic.exists()) rootDic.mkdirs();
	}
	public List<UserListBean> getUsers(String p){
		checkFiles(p);
		List<UserListBean> all = new ArrayList<UserListBean>();
		File[] f  = rootDic.listFiles();
		if(f.length!=0)
		for(File jitFile:f){
			if(jitFile.isFile()&&jitFile.getAbsolutePath().endsWith(".msu")){
				try {
					UserListBean userdata = new UserListBean();
					InputStream is = new FileInputStream(jitFile);
					String source[] = new String(AESUtils.decrypt(read(is),"mu7quG98EfK9hSAnlsT/uA==")).split("&");
					for(String s:source){
						System.out.println(s);
						if(s.startsWith("name::")){
							userdata.setUserNmae (s.replace("name::",""));
							userNames.add(s.replace("name::",""));
						}
						else if(s.startsWith("type::")){
							userdata.setUserType (Integer.parseInt((s.replace("type::",""))));
						}else if(s.startsWith("password::")){
							userdata.setUserPassword (s.replace("password::",""));
						}
					}
					all.add(userdata);
				} catch (Throwable e) {e.printStackTrace ();}
			}
		}
		return all;
	}
	public boolean saveUserData(String name, int Type,String pw){
		for(String jitS:userNames){
			if(name.equals(jitS)){
				return false;
			}
		}
		try{
		byte[] output = baseData.replace("@UserName",name).replace("@UserType",Type+"").replace("@Password",pw).getBytes();
		byte[] outputE = AESUtils.encrypt(output,"mu7quG98EfK9hSAnlsT/uA==");
		//FileOutputStream f = new FileOutputStream(rootDic.getAbsolutePath()+"/"+name+".msu");
			FileUtils.writeByteArrayToFile (new File (rootDic.getAbsolutePath()+"/"+name+".msu"),outputE);
		//f.write(outputE);
		//f.close();
		return true;
		}catch(Throwable t){
			t.printStackTrace ();
			return false;
		}
	}
	public static byte[] read(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                baos.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            baos.flush();
            return baos.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
