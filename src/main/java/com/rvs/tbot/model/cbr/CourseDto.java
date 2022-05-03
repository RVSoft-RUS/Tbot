package com.rvs.tbot.model.cbr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
public class CourseDto implements Serializable {

    @XmlElement(name = "Valute")
    private List<CourseDtoOnce> valute;
    @XmlElement(name = "Date")
    private LocalDate date;

    public List<CourseDtoOnce> getValute() {
        return valute;
    }

    @Override
    public String toString() {
        return "CourseDto{" +
                "valute=" + valute +
                ", date=" + date +
                '}';
    }
}
