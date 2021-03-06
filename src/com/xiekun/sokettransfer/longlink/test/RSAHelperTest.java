package com.xiekun.sokettransfer.longlink.test;

import com.xiekun.util.RSAHelper;


//RSA测试-下面代码直接用公私钥的base64编码串进行测试，当然也可以文件流读取pem文件，不过需要过滤掉第一行的“-----BEGIN *** KEY-----”和最后一行的“-----END *** KEY-----”。
public class RSAHelperTest {
	//贝付上线私钥
	public static final String privKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDZz128otoJRmQ0X7PuQNEvjhb9KOTOyeK603L4apm2nhGP6Ojx07iNXaxYkNaPgwBbTQ47roDXo7Q8iGzzqjqboYyXAwj1iZhc/GuD0VByT33CVXQ8IqW+P5mcY8bDsZUpx2Qa9qQM6Nz6gqNa5UQ/Ham4tlBhacACmCmmeZdm9gA8UJIrLUSMrqDi7JYJgSKFU/yG3W9TEHPDLyHdAvY+CicUGH90UdHCRqh+qnof3CeCwTC6BpI3tsNvXP4BVJTmGrmBKGszhxzbHF3Yk5v4nnioShkPoM/CfIy20auVb0iFEi/dhbcrSg41BOTqkIK6j+vFi1DoHZQ3tUdBAqSNAgMBAAECggEAQlfRW3XXJ+ATAYjtC3MexHdpNS9RklMYBoQFjvkZXPCEtOrimVY97oFLz6Y+VHQ4lZbA8RjUEdrBOXbsiMOfGmx4oD7CadfBWwFf1kTZVhg0XA8fWnnLNkm1Ble14UGkB5e+PCBIE86SEJPeIf+XbAoi+io+DY3ieGfUt+ptXWSc7smFV8OSKUB38DfaCFQsp0xe2Z7vH8Tq0KjEU6No+2vdf5r7yerZb3FWn0hq1RVfWXod8ANPu/RLRSq4+ToxS0aoOQ3h0QWKVUVgyLcjhiG7Vv09EyG3GtB9GCJaav1dihyw4jBwU/r5vkAXAh9qAZoeXjTz1SaIup4goy6/gQKBgQD2XGkBpQiVR6BVLG8YGMCN2yt64jCUIO1+WG5iN+xZwB4HKPAP93QGq2Sn9kpxMpMpHCKNb60iRiiLraK/kkpi/VKj8pmKp955LACNuXI2CAR8vkNDksldIGzvc3iPRH1rnc2PE9iA7+GZ36YlLEgb9twrh40Z/erp8+sf28NafQKBgQDiVPw1ybdLNBgNeu/A++GT+prsa0awEkEw5t9cTVjuU4ovn1WIozqMJMZGZFNJNYJj2znE1lmLCb3vNBHVHuXyVTmcpwaRj77BwnOVDSbVKX/7pf+z6KIR2oThqQbZGWsFTo11LHVkwOSZC/cwXTj8ITCvsc5GykKydScJmbt/UQKBgQCcm8AQ0JV1OBQfMODqY4m8Y6/vLOyOraPrplF9VGmF4E/2YzrAG6EBQGD7OOC3ttu/zWfVm0FnQUwzBs0nlNp+tOSc3fJnJzGQu70aWuHUoSIhHl3qDdy9u4gka7Ye9P35tFXg90+xH0y9oWOfy9eHF1V8wm2bMUPtNyanmO9FjQKBgHzsZY0WuonSxhfirCS7okW4Q/yMNacHXF4ugEiFFLnu+E3SxzY0yiMCRUIjK+kzlLWjEh64JRJeHSzDc7hLZqY8LiFt+20qhyKRPpSdfrqXPmHavOlDVPk/wKqBThtUMULrmZ58HIuxADwNY7GZMtPPSf0fyfg+uVOg4Bwnc1sRAoGAWAzF51wYTZMDaA0GJAYegocbGRFYwmewtbRZvj5BeFIB6K9akqRYLmw1oUtyOyvwHzENhI3xNyZGK8E4JTQoN2rd9qP0fLfg+6w2RBGh7JnEz+BCrYlwY+EttZTCAYBiPCe2g7ktrD4ddZ8I8+v9Sm9B6u/s/hg3Yp6oZ0RHvMk=";
	//贝付上线公钥
	public static final String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2c9dvKLaCUZkNF+z7kDRL44W/SjkzsniutNy+GqZtp4Rj+jo8dO4jV2sWJDWj4MAW00OO66A16O0PIhs86o6m6GMlwMI9YmYXPxrg9FQck99wlV0PCKlvj+ZnGPGw7GVKcdkGvakDOjc+oKjWuVEPx2puLZQYWnAApgppnmXZvYAPFCSKy1EjK6g4uyWCYEihVP8ht1vUxBzwy8h3QL2PgonFBh/dFHRwkaofqp6H9wngsEwugaSN7bDb1z+AVSU5hq5gShrM4cc2xxd2JOb+J54qEoZD6DPwnyMttGrlW9IhRIv3YW3K0oONQTk6pCCuo/rxYtQ6B2UN7VHQQKkjQIDAQAB";
	
	
//	//民生私钥
//	public static final String privKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDW+J3i8/zTkMvDyCftOsGNNVEmoodxUr23VeYUW9UI6do8U83ExJVtF9vX2O/RUru7ExJTMOxVg2QZh/IekFFv67iVPSfGBX70DC8vobZKEfhE8V82tWmCtH45zx6U73hVALiV6X8EslIosdT9xWAMY2NH0HvZ7V/06SvNNO0zKOm2vPyspzbwRDUpA89dmjEAwAlDx12/X+EYAR8VEU9ctLjVwYFXuFjc/BCryColK9HIg21P3vubHZM6GKIXfq6zJawV8lTkJlb5QEdcNk33VjpMhGs7ZRzJXgMG07uaPIBI6k7VQ2UZM7GtKUH8KBfWzViQ+Asz2cpE0t0VUoF9AgMBAAECggEBAKh9zwp+oDCW8g7vB9RZ1DDAlG2KwEwjRQ24pxBX9f75hBL6wHI0fsY2CBsDLtzLUtdLGHbaBrLzu/aC5lPsW9g0UsWuXElKL3pLPoS/5CfkM8qdwToZMKzAmZrn6xljJNbDLOpbTDI7Lkg1MjMBi8nJ8JvuHdTux+InDCzYCf6o51aZN2XMLmut4e5zEdjHHknV1On2Jjgc5Gu0CbzrLTUpwOlhICRGiWQY/Mnt73HJvZv7EQ0FjZk7UnWEweEfbgDBok283z6NaClvU5/MUaND2wWVdJfNPaUjHYtf7cE9S3rNrY5mpwVdAHmvAFKfx9PhB4A0hnY2h+i1VmLjBoECgYEA9542ca50sXNsbWwqW3T/78ZWKmQi9nP4MAtZkXzEFJtgEyIXT5CU6J8L1RhZ80J9tokR9FH3cwNUZl9/PiGD8l6jTjd08LfSW9t9z+4E0M6XelHG7ZfRtfV3HgzIHaBWI+nxd7yg4BoY21mgTmypz+88aPHDvC8Ss8T3jkJZul0CgYEA3j9+5JXt9sYbfw2Yln3XSRAXS9MDWRCNK/NZiQ4RZ22DpMOjNanH4jypIpuV1pnU8BM1iv4e5l49Kx2I5vqnE/GDkouhEzM8vPhBKHKLXloKtziIAR0lJOGGER2FtU3iuqSb7nRjt53G9OSnfJ2IL7H6Br7L0O+r679ofKiSsaECgYB2T0iyHmmxE3Yd/g1q70cN+FTZIkk2Ogi+Y93izpsdQXOxEJvUrz8Gul877MulmAJawbkrZDJ36IJd+4jfVcImfqNGTub30MyYiRHe1FnGrr7fec0zXlObvfGxEOhYh3BA7pkp3Z18FdwEihk2/2JPcH4LomAkPNWRwS2K8hbPHQKBgQCcBYdXgcmkzD7RWwIb5AwWxq0UFfbrt6rjh9r7VFzzdvZL3Ove6GnicSNroD34gdXzFAkqomue3dmjQwCw5pYUciAj6NITYIzrPHzBoGgmvJ95ML6JyaQh2BD+QvNy7FKXJKgzJpI6fREHKt5JpW3NzevwgFElRJw0zBLWMKGLAQKBgCXMuSed+xQk5q8of4SYAoHi+1JFQQw7GLa06PpphmlUJxj8WBijnpPJSfktLrgx9SAQ5Bfqw4cGiRsS7zIuc+FDrSSnQrHCwrRmgsaTm4kvqxglcbJAnbuNescAPPkvkK9ZI2gMsIHeHMi0lLSkkwO17IvdhztJbUzFhOSSiMe6";
//	//民生公钥
//	public static final String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1vid4vP805DLw8gn7TrBjTVRJqKHcVK9t1XmFFvVCOnaPFPNxMSVbRfb19jv0VK7uxMSUzDsVYNkGYfyHpBRb+u4lT0nxgV+9AwvL6G2ShH4RPFfNrVpgrR+Oc8elO94VQC4lel/BLJSKLHU/cVgDGNjR9B72e1f9OkrzTTtMyjptrz8rKc28EQ1KQPPXZoxAMAJQ8ddv1/hGAEfFRFPXLS41cGBV7hY3PwQq8gqJSvRyINtT977mx2TOhiiF36usyWsFfJU5CZW+UBHXDZN91Y6TIRrO2UcyV4DBtO7mjyASOpO1UNlGTOxrSlB/CgX1s1YkPgLM9nKRNLdFVKBfQIDAQAB";
//	
//	//贝付私钥
//	public static final String privKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDZ7k4T7Jdw8QHVkkU04hL4BNMNCiFVX8+DUrIf4XyPTAgcrB42sJxyHGJxyUD/F6OFqFcqw43POOm1AXM57TLdCLj2A0h1/OzHCs+SSDubnOi+qMJqDfxRxO5ORtJZLi+Xcb7VSYgruMfHwRz+1vR73YEm5cdw6+ZjHRP4h8lQeewpQNYBok9kAgOgB5MhI8gMqwcS+hrXusCY5fFdMRoOWQCmlP3cSSPLBzTnOmAUsOqDfR+9JBzUmuT5NSrL+I12ELTZ9NHr7OFP26oD9zQPx0BhfQaymt6f2ejYhH99Sr1RSlymIheVhgdd27eC6m4h20twqD3C5mkhuDlpMy0nAgMBAAECggEABKsySQL8i5LeMzEbAnMFbQLNcq4Q4L9YtruCzWQLQxah5dtaGlnWQCuTgFSLa+uZuTdOBsCBvTq+5v3M9iS6vjl9nOedGcn9WJZGtB+QB1lpMwxyfcsm6KmLYWnq/7YZluj86ZzVqfofbYwZjuwT+MNaDPdHOogTO3RYBS8DhRHNenz/6XERFvLaZt7Erp0Hayh0yRLZTrCKAerKU0glfmj4JlyEWyeRfULoijh3ppyHsIxWWjPQpB4dqvqnEffNg0PZE8YVnDRLpyQo+0UZ3uBQ/qbgOQd4S70e1v4snCbJ9GUxb7ppALHUmYXP5RwC3Ua/DHLK44oc09arHKcZgQKBgQDuLH9Zx1ScEFBgNdpOXMBzGeXH0R34IadA2jGPVZw+GDBRvjXf/Mjv8Io/ChExuVWuFaBymU2acaXzTgx6/3hD+NlrzlibmyMzv99m0W4h7FbQh2nOEZpl7W43y4D4Ez9mk2yAquGs5qZdBK0K5dNCmkTbhRBbaNdNSBFioB06cQKBgQDqPfHp6eWsB9o8Q07mbgNqy2ihcJrKLhTUoWKm8IVmzN3SxIMWo7VTyxlw6tWxzo9UTQld6F2mIwBJuDqL8qHufbJ7cGmikhTTl1AdYBq7Wb3FX5GaYOAjggaSZc8Wq8uAdovWtUMzaoctv5SGJ67g5taPbc9va2F1NC6wQQY9FwKBgQCaM8cUGmEmVc2kLNm1iIm1O9wwXaz4nNlLvLj/ox7wePq3wd3hsilqZ6MqI1t0hRj7B9eEusqqptAZ0bt02vhDkdc63DM0M+I7vAto50lVv02J31Jab702Jt4IU8KhWtzeja5fM8O/DOpDKSbb+maoJXdVZTZs6FkVGQbggufJkQKBgFAbwTj7GeXchaEob7pUk2yZhf34MafFXLBfRQrMpQLEO3SSajZ65E903Q2F8nEtsEznT7XC+wwPWlDb5YHpB8I1UK3wgt/rT54RWH/DV0ENbt4qUfRW/zCFjlPQ56LcO+uCH07djM7vtq1JoLTEq4UUzPoeVtvHAHyBzRnQEx8fAoGAG8YMQUeyytwt5GxTYn+YbioGVrSXYjBuzUd9lQXikHUcIQw7bshQhGMA/T9duhxKQDqSUYeCLcvQ2b3Z4xMjaoHi6FgYcxkdCTQOv/qpQIq69PgTZ/mHQ0rXV1HpYJPeMTIqtKgaDUujBVdZ60PGY9qkdOjtSC7/KKcNqEToEow=";
//
//	//贝付公钥
//	public static final String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2e5OE+yXcPEB1ZJFNOIS+ATTDQohVV/Pg1KyH+F8j0wIHKweNrCcchxicclA/xejhahXKsONzzjptQFzOe0y3Qi49gNIdfzsxwrPkkg7m5zovqjCag38UcTuTkbSWS4vl3G+1UmIK7jHx8Ec/tb0e92BJuXHcOvmYx0T+IfJUHnsKUDWAaJPZAIDoAeTISPIDKsHEvoa17rAmOXxXTEaDlkAppT93Ekjywc05zpgFLDqg30fvSQc1Jrk+TUqy/iNdhC02fTR6+zhT9uqA/c0D8dAYX0Gspren9no2IR/fUq9UUpcpiIXlYYHXdu3gupuIdtLcKg9wuZpIbg5aTMtJwIDAQAB";

	public static final String plaintext = "你好，测试";

	public static void main(String[] args) throws Exception {

		System.out.println("=====> init <=====");
		RSAHelper cipher = new RSAHelper();
		cipher.initKey(privKey, pubKey, 2048);

		System.out.println("=====> sign & verify <=====");

		// 签名
		byte[] signBytes = cipher.signRSA(plaintext.getBytes("UTF-8"), false,
				"UTF-8");

		// 验证签名
		boolean isValid = cipher.verifyRSA(plaintext.getBytes("UTF-8"),
				signBytes, false, "UTF-8");
		System.out.println("isValid: " + isValid);

		// 加密和解密
		System.out.println("=====> encrypt & decrypt <=====");
		// 对明文加密
		byte[] cryptedBytes = cipher.encryptRSA(plaintext.getBytes("UTF-8"),
				false, "UTF-8");

		// 对密文解密
		byte[] decryptedBytes = cipher.decryptRSA(cryptedBytes, false, "UTF-8");
		System.out.println("decrypted: " + new String(decryptedBytes, "UTF-8"));
	}
}
