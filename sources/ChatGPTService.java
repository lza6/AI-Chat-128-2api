package com.starnest.keyboard.model.remote;

import Ig.f;
import Nc.C1024s;
import com.facebook.AuthenticationToken;
import com.facebook.GraphRequest;
import com.starnest.keyboard.model.model.ImageGeneratorRequest;
import com.starnest.keyboard.model.model.ImageGeneratorResponse;
import com.starnest.keyboard.model.model.ResponseData;
import com.starnest.keyboard.model.model.ResponseEmbedding;
import com.starnest.keyboard.model.model.ResponseUploadFile;
import com.starnest.keyboard.model.model.SearchRequest;
import com.starnest.keyboard.model.model.YoutubeTranscriptRequest;
import com.starnest.keyboard.model.model.YoutubeTranscriptResponse;
import com.starnest.keyboard.model.model.h;
import com.starnest.keyboard.model.model.i;
import java.util.Map;
import kotlin.Metadata;
import p368wh.B;
import p368wh.Q;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J5\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0014\b\u0001\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00022\b\b\u0001\u0010\u0006\u001a\u00020\u0005H'¢\u0006\u0004\b\t\u0010\nJ5\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0014\b\u0001\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00022\b\b\u0001\u0010\u0006\u001a\u00020\u000bH'¢\u0006\u0004\b\t\u0010\fJ5\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0014\b\u0001\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00022\b\b\u0001\u0010\u0006\u001a\u00020\u0005H'¢\u0006\u0004\b\r\u0010\nJ5\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0014\b\u0001\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00022\b\b\u0001\u0010\u0006\u001a\u00020\u0005H'¢\u0006\u0004\b\u000e\u0010\nJ5\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0014\b\u0001\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00022\b\b\u0001\u0010\u0006\u001a\u00020\u0005H'¢\u0006\u0004\b\u000f\u0010\nJ\u001f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00110\u00072\b\b\u0001\u0010\u0006\u001a\u00020\u0010H'¢\u0006\u0004\b\u0012\u0010\u0013J\u001f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00160\u00072\b\b\u0001\u0010\u0015\u001a\u00020\u0014H'¢\u0006\u0004\b\u0017\u0010\u0018J5\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00110\u00072\u0014\b\u0001\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00022\b\b\u0001\u0010\u0006\u001a\u00020\u000bH'¢\u0006\u0004\b\u0019\u0010\fJ#\u0010\u001c\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\u00072\b\b\u0001\u0010\u001b\u001a\u00020\u001aH'¢\u0006\u0004\b\u001c\u0010\u001dJ\u001f\u0010!\u001a\b\u0012\u0004\u0012\u00020 0\u00072\b\b\u0001\u0010\u001f\u001a\u00020\u001eH'¢\u0006\u0004\b!\u0010\"J\u001f\u0010%\u001a\b\u0012\u0004\u0012\u00020$0\u00072\b\b\u0001\u0010\u001f\u001a\u00020#H'¢\u0006\u0004\b%\u0010&J\u001f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0001\u0010'\u001a\u00020\u0003H'¢\u0006\u0004\b(\u0010)J \u0010-\u001a\b\u0012\u0004\u0012\u00020,0+2\b\b\u0001\u0010*\u001a\u00020\u001aH§@¢\u0006\u0004\b-\u0010.J \u0010/\u001a\b\u0012\u0004\u0012\u00020,0\u00072\b\b\u0001\u0010\u0006\u001a\u00020\u0005H§@¢\u0006\u0004\b/\u00100J5\u00101\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0014\b\u0001\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00022\b\b\u0001\u0010\u0006\u001a\u00020\u000bH'¢\u0006\u0004\b1\u0010\fJ5\u00102\u001a\b\u0012\u0004\u0012\u00020\u00110\u00072\u0014\b\u0001\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00022\b\b\u0001\u0010\u0006\u001a\u00020\u000bH'¢\u0006\u0004\b2\u0010\f¨\u00063À\u0006\u0003"}, d2 = {"Lcom/starnest/keyboard/model/remote/ChatGPTService;", "", "", "", AuthenticationToken.HEADER_KEY, "Lcom/starnest/keyboard/model/model/h;", "request", "Lretrofit2/Call;", "Lcom/starnest/keyboard/model/model/ResponseData;", "createChatCompletion", "(Ljava/util/Map;Lcom/starnest/keyboard/model/model/h;)Lretrofit2/Call;", "Lcom/starnest/keyboard/model/model/i;", "(Ljava/util/Map;Lcom/starnest/keyboard/model/model/i;)Lretrofit2/Call;", "getSuggestions", "askYoutube", "generateStory", "Lcom/starnest/keyboard/model/model/SearchRequest;", "Lwh/Q;", GraphRequest.SEARCH, "(Lcom/starnest/keyboard/model/model/SearchRequest;)Lretrofit2/Call;", "LNc/s;", "chunk", "Lcom/starnest/keyboard/model/model/ResponseEmbedding;", "createEmbeddings", "(LNc/s;)Lretrofit2/Call;", "createChatCompletionStream", "Lwh/B;", "part", "uploadFile", "(Lwh/B;)Lretrofit2/Call;", "Lcom/starnest/keyboard/model/model/ImageGeneratorRequest;", "body", "Lcom/starnest/keyboard/model/model/ImageGeneratorResponse;", "generateImage", "(Lcom/starnest/keyboard/model/model/ImageGeneratorRequest;)Lretrofit2/Call;", "Lcom/starnest/keyboard/model/model/YoutubeTranscriptRequest;", "Lcom/starnest/keyboard/model/model/YoutubeTranscriptResponse;", "getYoutubeTranscript", "(Lcom/starnest/keyboard/model/model/YoutubeTranscriptRequest;)Lretrofit2/Call;", "generationId", "getImageGeneration", "(Ljava/lang/String;)Lretrofit2/Call;", "file", "Lretrofit2/Response;", "Lcom/starnest/keyboard/model/model/ResponseUploadFile;", "uploadFileGemini", "(Lwh/B;LIg/f;)Ljava/lang/Object;", "getFile", "(Lcom/starnest/keyboard/model/model/h;LIg/f;)Ljava/lang/Object;", "askPdf", "chatStreamPdf", "keyboard_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface ChatGPTService {
    @POST("/api/v2/files/completions")
    Call<ResponseData> askPdf(@HeaderMap Map<String, String> header, @Body i request);

    @POST("/api/v2/completions/v3")
    Call<ResponseData> askYoutube(@HeaderMap Map<String, String> header, @Body h request);

    @Streaming
    @POST("/api/v2/files/completions/stream")
    Call<Q> chatStreamPdf(@HeaderMap Map<String, String> header, @Body i request);

    @POST("/api/v2/completions/v2")
    Call<ResponseData> createChatCompletion(@HeaderMap Map<String, String> header, @Body h request);

    @POST("/api/v2/completions/v2")
    Call<ResponseData> createChatCompletion(@HeaderMap Map<String, String> header, @Body i request);

    @Streaming
    @POST("/api/v2/completions/v3/stream")
    Call<Q> createChatCompletionStream(@HeaderMap Map<String, String> header, @Body i request);

    @POST("/api/v2/embeddings")
    Call<ResponseEmbedding> createEmbeddings(@Body C1024s chunk);

    @POST("/api/v2/image-generator")
    Call<ImageGeneratorResponse> generateImage(@Body ImageGeneratorRequest body);

    @POST("/api/v2/completions/v3")
    Call<ResponseData> generateStory(@HeaderMap Map<String, String> header, @Body h request);

    @POST("/api/v2/files/get")
    Object getFile(@Body h hVar, f<? super Call<ResponseUploadFile>> fVar);

    @GET("/api/v2/image-generator/{generationId}")
    Call<Object> getImageGeneration(@Path("generationId") String generationId);

    @POST("/api/v2/completions/v3")
    Call<ResponseData> getSuggestions(@HeaderMap Map<String, String> header, @Body h request);

    @POST("/api/v2/youtube/transcript")
    Call<YoutubeTranscriptResponse> getYoutubeTranscript(@Body YoutubeTranscriptRequest body);

    @POST("/api/v2/search")
    Call<Q> search(@Body SearchRequest request);

    @POST("/api/v2/upload")
    @Multipart
    Call<Object> uploadFile(@Part B part);

    @POST("/api/v2/files/upload")
    @Multipart
    Object uploadFileGemini(@Part B b10, f<? super Response<ResponseUploadFile>> fVar);
}
