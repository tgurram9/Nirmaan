package bphc.com.nirmaan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bphc.com.nirmaan.object.Subjects;
import bphc.com.nirmaan.object.Topic;
import bphc.com.nirmaan.object.TopicList;
import bphc.com.nirmaan.object.VolunteerList;
import bphc.com.nirmaan.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatorAssigningFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    AutoCompleteTextView studentSelect;
    Spinner class_select,subject_select,topic_select;
    TextView dateandtime;
    ArrayAdapter<String> classAdapter;
    ArrayAdapter<String> volunteerAdapter;
    ArrayList<String> names,classes=null,subjects=null,topics=null;
    SingleDateAndTimePickerDialog.Builder picker;
    ArrayAdapter<String> subjectsAdapter = null,topicsAdapter = null;
    String selectedClass="",selectedSubject="",selectedTopic="";
    List<Topic> topicList = null;

    public CreatorAssigningFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        names = new ArrayList<>();
        subjects = new ArrayList<>();
        subjects.add("Select a class for subjects:");
        topics =new ArrayList<>();
        topics.add("Select a subject for topics:");
        classes = new ArrayList<>();
        classes.add(0,"1");
        classes.add(1,"2");
        classes.add(2,"3");
        classes.add(3,"4");
        classes.add(4,"5");
        classes.add(5,"6");
        classes.add(6,"7");
        classes.add(7,"8");
        classes.add(8,"9");
        classes.add(9,"10");

        return inflater.inflate(R.layout.fragment_creator_assigning, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        studentSelect = (AutoCompleteTextView) view.findViewById(R.id.search_volunteer);
        class_select = (Spinner) view.findViewById(R.id.class_spinner);
        subject_select = (Spinner) view.findViewById(R.id.subject_spinner);
        topic_select = (Spinner) view.findViewById(R.id.topic_spinner);
        dateandtime = (TextView) view.findViewById(R.id.input_date_time);

        dateandtime.setInputType(InputType.TYPE_NULL);
        subject_select.setEnabled(false);
        topic_select.setEnabled(false);

        picker = new SingleDateAndTimePickerDialog.Builder(getActivity())
                .curved()
                .minutesStep(15)
                .title("Scroll to change")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        dateandtime.setText(date.toString());
                    }
                });

        dateandtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.display();
            }
        });

        studentSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("names",charSequence.toString());

                if(volunteerAdapter!=null) {
                    volunteerAdapter.clear();
                }

                Call<VolunteerList> volunteerListCall = ApiManager.getInstance().getService()
                        .getVolunteers("nirmaan", "supersix", charSequence.toString());

                volunteerListCall.enqueue(new Callback<VolunteerList>() {
                    @Override
                    public void onResponse(Call<VolunteerList> call, Response<VolunteerList> response) {
                        for(int i=0;i<response.body().getVolunteers().size();i++){
                            names.add(i,response.body().getVolunteers().get(i).getName());
                            Log.e("names",names.get(i));
                        }
                        volunteerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, names);
                        studentSelect.setAdapter(volunteerAdapter);
                        volunteerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<VolunteerList> call, Throwable t) {
                        Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        classAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item, classes);
        class_select.setAdapter(classAdapter);

        subjectsAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,subjects);
        subject_select.setAdapter(subjectsAdapter);

        topicsAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,topics);
        topic_select.setAdapter(topicsAdapter);

        subject_select.setAdapter(subjectsAdapter);
        topic_select.setAdapter(topicsAdapter);

        if(!subjects.get(0).equals("Select a class for subjects:")){
            subject_select.setEnabled(true);
            subjectsAdapter.notifyDataSetChanged();
        }

        if (!topics.get(0).equals("Select a subject for topics:")){
            topic_select.setEnabled(true);
            topicsAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (view.getId()){
            case R.id.class_spinner:
                selectedClass = classes.get(i);
                Call<Subjects> subjectsCall = ApiManager.getInstance().getService().getSubjects(selectedClass);
                subjectsCall.enqueue(new Callback<Subjects>() {
                    @Override
                    public void onResponse(Call<Subjects> call, Response<Subjects> response) {
                        subjects = (ArrayList<String>) response.body().getSubjects();
                        subject_select.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<Subjects> call, Throwable t) {
                        Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.subject_spinner:
                selectedSubject = subjects.get(i);
                Call<TopicList> topicListCall = ApiManager.getInstance().getService().getTopics(selectedClass,selectedSubject);
                topicListCall.enqueue(new Callback<TopicList>() {
                    @Override
                    public void onResponse(Call<TopicList> call, Response<TopicList> response) {
                        topicList = response.body().getTopics();
                        for(int i=0;i<topicList.size();i++){
                            topics.add(i,topicList.get(i).getChapterName());
                        }
                    }

                    @Override
                    public void onFailure(Call<TopicList> call, Throwable t) {
                        Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.topic_spinner:
                selectedTopic = subjects.get(i);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}