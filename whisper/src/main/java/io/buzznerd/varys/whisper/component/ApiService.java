package io.buzznerd.varys.whisper.component;


import io.buzznerd.varys.whisper.model.network.UploadRequest;
import io.buzznerd.varys.whisper.model.network.WhisperResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public interface ApiService {

    @POST("adddata")
    Call<WhisperResponse> doUpload(@Body UploadRequest uploadRequest);
}
