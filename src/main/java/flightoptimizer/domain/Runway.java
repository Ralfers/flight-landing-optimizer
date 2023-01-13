package flightoptimizer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Runway {

    private int id;

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
