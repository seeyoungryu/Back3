package com.example.withdogandcat.domain.naverApi;

import com.example.withdogandcat.global.exception.CustomException;
import com.example.withdogandcat.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

@Component
public class NaverSearchApi {

    @Value("${NAVER-CLIENT}")
    private String clientId;

    @Value("${NAVER-KEY}")
    private String clientSecret;

    public String search(String query) {
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(ErrorCode.FAIL_TO_ENCODING);
        }

        // 뉴스 검색결과 가져오기
        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + query;

        HttpURLConnection con = connect(apiURL);
        try {
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                throw new IOException("서버 응답 에러: " + readBody(con.getErrorStream()));
            }
        } catch (IOException e) {
            throw new CustomException(ErrorCode.CONNECTION_FAILED);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body) {
        try (InputStreamReader streamReader = new InputStreamReader(body);
             BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.CONNECTION_FAILED);
        }
    }
}
