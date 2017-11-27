package de.tub.util;

import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.extras.codecs.MappingCodec;
import org.joda.time.DateTime;

import java.util.Date;

public class JodaTimeCodec extends MappingCodec<DateTime,Date> {

    public JodaTimeCodec(){
        super(TypeCodec.timestamp(),DateTime.class);
    }

    @Override
    protected DateTime deserialize(Date date) {
        return new DateTime(date.getTime());
    }

    @Override
    protected Date serialize(DateTime o) {
        return o.toDate();
    }
}
