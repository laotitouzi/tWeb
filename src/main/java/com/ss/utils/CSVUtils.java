package com.ss.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ss.entity.Order;

public class CSVUtils {

	public static List<Order> process(InputStream is) {
		List<Order> list = null;
		BufferedReader br = null;
		String line = null;
		if (is == null)
			return null;
		br = new BufferedReader(new InputStreamReader(is));
		list = new ArrayList<Order>();
		int index = 0;
		try {
			while ((line = br.readLine()) != null) {
				index++;
				if (index < 6)
					continue;

				String[] orderArr = line.split(",");

				for (int j = 0; j < orderArr.length; j++) {
					Order o = new Order();

					o.setOrderNo(StringUtils.trim(orderArr[0]));
					o.setBuyer(StringUtils.trim(orderArr[7]));
					o.setCreateDate(DateUtils.parse(StringUtils.trim(orderArr[2])));
					BigDecimal b1 = new BigDecimal(StringUtils.trim(orderArr[9]));
					String serviceFee = "0", backFee = "0";

					if (StringUtils.isNotBlank(orderArr[12])) {
						serviceFee = StringUtils.trim(orderArr[12]);
					}
					if (StringUtils.isNotBlank(orderArr[13])) {
						backFee = StringUtils.trim(orderArr[13]);
					}

					o.setFee(b1.subtract(new BigDecimal(serviceFee)).subtract(new BigDecimal(backFee)).doubleValue());
					o.setGoodsName(StringUtils.trim(orderArr[8]));
					list.add(o);
				}
			}

			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static boolean exportCsv(File file, List<String> dataList) {
		boolean isSucess = false;

		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			out = new FileOutputStream(file);
			osw = new OutputStreamWriter(out);
			bw = new BufferedWriter(osw);
			if (dataList != null && !dataList.isEmpty()) {
				for (String data : dataList) {
					bw.append(data).append("\r");
				}
			}
			isSucess = true;
		} catch (Exception e) {
			isSucess = false;
		} finally {
			if (bw != null) {
				try {
					bw.close();
					bw = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (osw != null) {
				try {
					osw.close();
					osw = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return isSucess;
	}
}
