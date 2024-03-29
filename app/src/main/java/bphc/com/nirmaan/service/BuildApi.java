package bphc.com.nirmaan.service;

import bphc.com.nirmaan.object.CreatorData;
import bphc.com.nirmaan.object.LoginSet;
import bphc.com.nirmaan.object.StudentData;
import bphc.com.nirmaan.object.Subjects;
import bphc.com.nirmaan.object.TopicList;
import bphc.com.nirmaan.object.VolQuestions;
import bphc.com.nirmaan.object.VolScheduleCollections;
import bphc.com.nirmaan.object.VolunteerList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by tejeshwar on 30/9/16.
 */

public interface BuildApi {

    @GET("login.php")
    Call<LoginSet> authenticateUser(@Query("name") String name,
                                    @Query("password") String password);

    @GET("volunteer_questions.php")
    Call<VolQuestions> getUserdata(@Query("name") String name,
                                   @Query("password") String password);

    @GET("volunteer_schedule.php")
    Call<VolScheduleCollections> getScheduleSet(@Query("name") String name,
                                                @Query("password") String password);

    @GET("student_data.php")
    Call<StudentData> getStudentData(@Query("name") String name,
                                     @Query("password") String password);

    @GET("volunteer_search.php")
    Call<VolunteerList> getVolunteers(@Query("name") String name,
                                      @Query("password") String password,
                                      @Query("key") String key);

    @GET("subject_data.php")
    Call<Subjects> getSubjects(@Query("class") String classname);

    @GET("subject_data.php")
    Call<TopicList> getTopics(@Query("class") String classname,
                              @Query("subject") String subject);

    @GET("admin_data.php")
    Call<CreatorData> getAdminData(@Query("name") String name,
                                   @Query("password") String password);

    @FormUrlEncoded
    @POST("admin_post.php")
    Call<String> getPostOutput(@Field("name") String vol_id,
                                @Field("topic_id") String topic_id,
                                @Field("datetime") long datetime);




}
