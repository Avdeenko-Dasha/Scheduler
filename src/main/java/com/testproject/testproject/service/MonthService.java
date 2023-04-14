package com.testproject.testproject.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@NoArgsConstructor
public class MonthService {

    public String[] getNamesAllMonth() {
        return new String[]{"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    }

    public Integer getNumberMonth(String selectedItem) {
        Integer numberMonth = 0;
        switch (selectedItem) {
            case "Январь":
                numberMonth = 1;
                break;
            case "Февраль":
                numberMonth = 2;
                break;
            case "Март":
                numberMonth = 3;
                break;
            case "Апрель":
                numberMonth = 4;
                break;
            case "Май":
                numberMonth = 5;
                break;
            case "Июнь":
                numberMonth = 6;
                break;
            case "Июль":
                numberMonth = 7;
                break;
            case "Август":
                numberMonth = 8;
                break;
            case "Сентябрь":
                numberMonth = 9;
                break;
            case "Октябрь":
                numberMonth = 10;
                break;
            case "Ноябрь":
                numberMonth = 11;
                break;
            case "Декабрь":
                numberMonth = 12;
                break;
            default:
                break;
        }
        return numberMonth;
    }
}
