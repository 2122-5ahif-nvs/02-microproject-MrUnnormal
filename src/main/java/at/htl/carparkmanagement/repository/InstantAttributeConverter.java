package at.htl.carparkmanagement.repository;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;

@Converter
public class InstantAttributeConverter implements AttributeConverter<Instant, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(Instant instant) {
        if (instant == null)
            return null;
        else
        {
            return Timestamp.from(instant);
        }
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp timestamp) {
        //System.out.println(timestamp + " " + timestamp.toInstant() + " " + timestamp.getTimezoneOffset());
        return (timestamp == null ? null : timestamp.toInstant());
    }
}
