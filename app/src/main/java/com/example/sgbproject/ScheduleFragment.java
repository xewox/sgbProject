package com.example.sgbproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ScheduleFragment extends Fragment {
    //사용할 객체 변수들
    TextView textview_todo;
    Button saveBtn;

    String fileName = null;
    String str = null;

    //리스트 뷰를 위한 클래스 변수 선언
    private ListView sch_ListView;
    private ArrayAdapter<String> sch_Adapter;
    private ArrayList<String> items = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        String date = bundle.getString("Date");

        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        String[] splitData = date.split("-");


        textview_todo = v.findViewById(R.id.editTodo);
        saveBtn = v.findViewById(R.id.saveBtn);
        //리스트뷰와 어댑터 초기화
        sch_ListView = v.findViewById(R.id.list);
        sch_Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice, items);
        sch_ListView.setAdapter(sch_Adapter);



        try {
            FileInputStream fis = v.getContext().openFileInput("1_" + date + ".txt");
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String temp;
            while ((temp = bufferReader.readLine()) != null) {
                Log.v(temp, "현재문구" + temp);
                sch_Adapter.add(temp);

            }
        } catch (Exception e) {

        }
        sch_Adapter.notifyDataSetChanged();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textview_todo.getText().toString() == "") {

                }
                try {
                    //파일 이름 만들기
                    fileName = "1_" + date + ".txt";
                    //파일생성 - 추가 갱신 저장
                    FileOutputStream fos = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
                    str = textview_todo.getText().toString() + "\n";
                    if (textview_todo.getText().toString().length() < 1) {
                        Toast.makeText(getActivity(), "할 일을 입력하세요", Toast.LENGTH_SHORT).show();
                    } else {
                        items.add(str);
                        textview_todo.setText("");
                        fos.write(str.getBytes(StandardCharsets.UTF_8));
                        fos.close();
                        Toast.makeText(getActivity(), "추가완료", Toast.LENGTH_SHORT).show();
                        sch_Adapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {

                }
            }


        });

        sch_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                items.remove(position);
                sch_ListView.clearChoices();

                try{
                    fileName = "1_" + date + ".txt";
                    //파일생성 - 추가 갱신 저장
                    String newData="";
                    for(int i = 0 ; i < items.size()-1 ; i++) {
                        newData += items.get(i)+"\n";
                        Toast.makeText(getActivity(), newData, Toast.LENGTH_SHORT).show();
                        newData.replace("\n\n","\n");
                    }


                    if(items.size() > 1) {
                        newData += items.get(items.size() - 1);
                    }
                    FileOutputStream fos = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                    fos.write(newData.getBytes(StandardCharsets.UTF_8));
                    fos.flush();
                    fos.close();
                }catch (IOException e){

                }
                sch_Adapter.notifyDataSetChanged();
            }
        });
        return v;
    }
}