package io.renren.others.remote;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import io.renren.entity.template.RapDataEntity;
import io.renren.entity.template.RapResponseInterfece;
import io.renren.entity.template.RapResponseRepository;
import io.renren.entity.template.RapUpdateReqBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.http.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RetrofitClient(baseUrl = "${rap.baseUrl}")
public interface HtppApi {


     String cookie="experimentation_subject_id=ImU0ZTcyM2QxLTc5OWYtNDJiYS1iMWExLWMzYmQ0MDJhOGM3NyI%3D--22248a74eb74343389e7c8e63f22b3ab4c7a1545; koa.sid=AS3ZgewTifLwj0FHX3-fzH0SOt-U2AxP; koa.sid.sig=L6Kudu6v4Bc50xUMi3KwiQ7hMT0";

    @GET("/interface/get")
    @Headers("Cookie: "+cookie)
    public RapResponseInterfece getInterfaceInformation(@Query("id") Long id);
    @GET("/repository/get")
    @Headers("Cookie: "+cookie)
    public RapResponseRepository getRepositoryInformation(@Query("id") Long id, @Query("excludeProperty")boolean excludeProperty);

    @POST("/properties/update")
    @Headers("Cookie: "+cookie)
    public HashMap update(@Query("itf") Long itf, @Body RapUpdateReqBody reqBody);
}
