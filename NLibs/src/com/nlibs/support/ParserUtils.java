package com.nlibs.support;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;

/**
 * Lớp tiện ích, dùng để parse chuỗi dữ liệu trả về từ server sang 1 định dạng
 * theo nhu cầu
 * 
 * @author Chau Minh Nhut(chauminhnhut90@gmail.com)
 * @since 13/10/2014
 * @version 1.0
 * 
 */
public class ParserUtils {

	/**
	 * Parse 1 chuỗi dữ liệu thành 1 danh sách
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static <T> List<T> parseObjectFromString(String jsonStr, Type type) {
		/*
		String jsonStr = [{"id":"c200","gender":"male","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"ravi@gmail.com","name":"Ravi Tamada"},{"id":"c201","gender":"male","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"johnny_depp@gmail.com","name":"Johnny Depp"},{"id":"c202","gender":"male","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"leonardo_dicaprio@gmail.com","name":"Leonardo Dicaprio"},{"id":"c203","gender":"male","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"john_wayne@gmail.com","name":"John Wayne"},{"id":"c204","gender":"female","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"angelina_jolie@gmail.com","name":"Angelina Jolie"},{"id":"c205","gender":"female","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"dido@gmail.com","name":"Dido"},{"id":"c206","gender":"female","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"adele@gmail.com","name":"Adele"},{"id":"c207","gender":"male","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"hugh_jackman@gmail.com","name":"Hugh Jackman"},{"id":"c208","gender":"male","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"will_smith@gmail.com","name":"Will Smith"},{"id":"c209","gender":"male","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"clint_eastwood@gmail.com","name":"Clint Eastwood"},{"id":"c2010","gender":"male","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"barack_obama@gmail.com","name":"Barack Obama"},{"id":"c2011","gender":"female","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"kate_winslet@gmail.com","name":"Kate Winslet"},{"id":"c2012","gender":"male","phone":{"office":"00 000000","home":"00 000000","mobile":"+91 0000000000"},"address":"xx-xx-xxxx,x - street, x - country","email":"eminem@gmail.com","name":"Eminem"}]
		 */
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, type);
	}
}
