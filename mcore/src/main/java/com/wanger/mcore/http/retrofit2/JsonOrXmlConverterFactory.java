package com.wanger.mcore.http.retrofit2;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

import io.reactivex.annotations.Nullable;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jaxb.JaxbConverterFactory;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author wanger
 * @date 2018/12/17 12:08
 * @email xxx@gmail.com
 * @desc doc
 */
public class JsonOrXmlConverterFactory extends Converter.Factory {
    private final Converter.Factory xmlFactory = JaxbConverterFactory.create();
    private final Converter.Factory jsonFactory = GsonConverterFactory.create();

    public static JsonOrXmlConverterFactory create() {
        return new JsonOrXmlConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        for (Annotation annotation : annotations) {
            if (!(annotation instanceof ResponseFormat)) {
                continue;
            }
            String value = ((ResponseFormat) annotation).value();
            if (ResponseFormat.JSON.equals(value)) {
                return jsonFactory.responseBodyConverter(type, annotations, retrofit);
            } else if (ResponseFormat.XML.equals(value)) {
                return xmlFactory.responseBodyConverter(type, annotations, retrofit);
            }
        }
        return super.responseBodyConverter(type, annotations, retrofit);
    }

    @Target(METHOD)
    @Retention(RUNTIME)
    public @interface ResponseFormat {

        String JSON = "json";

        String XML = "xml";

        String value() default "json";
    }
}
