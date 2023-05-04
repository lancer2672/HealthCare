package com.example.healthcareapp.viewmodels;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SurveyViewModel extends ViewModel {
    private List<String> questionList = new ArrayList<>(
            Arrays.asList( "Bạn có khó khăn trong việc tập trung hoặc quên lãng những việc quan trọng không?",
                    "Bạn có cảm giác căng thẳng, lo lắng hoặc sợ hãi không?",
                    "Bạn có cảm giác mệt mỏi hoặc thiếu năng lượng không?",
                    "Bạn có khó khăn trong việc thực hiện các hoạt động hàng ngày như công việc hoặc học tập không?",
                    "Bạn có khó khăn trong việc kiểm soát cảm xúc của mình không?",
                    "Bạn có cảm thấy áp lực hay căng thẳng trong việc giải quyết các vấn đề trong cuộc sống không?")
    );
    private int currentQuestionIndex = -1;
    public List<String> getQuestionList() {
        return questionList;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }
}
