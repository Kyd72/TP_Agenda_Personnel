package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Description : A repetitive event that terminates after a given date, or after
 * a given number of occurrences
 */
public class FixedTerminationEvent extends RepetitiveEvent {

    private LocalDate terminationInclusive = null;
    private long numberOfOccurrences = 0 ;
    /**
     * Constructs a fixed terminationInclusive event ending at a given date
     *
     * @param title the title of this event
     * @param start the start time of this event
     * @param duration the duration of this event
     * @param frequency one of :
     * <UL>
     * <LI>ChronoUnit.DAYS for daily repetitions</LI>
     * <LI>ChronoUnit.WEEKS for weekly repetitions</LI>
     * <LI>ChronoUnit.MONTHS for monthly repetitions</LI>
     * </UL>
     * @param terminationInclusive the date when this event ends
     */
    public FixedTerminationEvent(String title, LocalDateTime start, Duration duration, ChronoUnit frequency, LocalDate terminationInclusive) {
         super(title, start, duration, frequency);

      this.terminationInclusive = terminationInclusive;

    }

    /**
     * Constructs a fixed termination event ending after a number of iterations
     *
     * @param title the title of this event
     * @param start the start time of this event
     * @param duration the duration of this event
     * @param frequency one of :
     * <UL>
     * <LI>ChronoUnit.DAYS for daily repetitions</LI>
     * <LI>ChronoUnit.WEEKS for weekly repetitions</LI>
     * <LI>ChronoUnit.MONTHS for monthly repetitions</LI>
     * </UL>
     * @param numberOfOccurrences the number of occurrences of this repetitive event
     */
    public FixedTerminationEvent(String title, LocalDateTime start, Duration duration, ChronoUnit frequency, long numberOfOccurrences) {
        super(title, start, duration, frequency);
        this.numberOfOccurrences = numberOfOccurrences;
    }

    /**
     *
     * @return the termination date of this repetitive event
     */
    public LocalDate getTerminationDate() {
        if(terminationInclusive!=null) {
            return terminationInclusive.atStartOfDay().toLocalDate();
        }
        else {
            LocalDate dateFin=getStart().toLocalDate().plus(numberOfOccurrences-1,getFrequency());
            return dateFin.atStartOfDay().toLocalDate();
        }

    }

    public long getNumberOfOccurrences() {
        if(numberOfOccurrences==0&&terminationInclusive!=null){
            long joursDiff= ChronoUnit.DAYS.between(getStart().toLocalDate(),getTerminationDate());
            numberOfOccurrences=1+(joursDiff/getFrequency().getDuration().toDays());
        }

        return numberOfOccurrences;
    }

    /**
     * Tests if an event occurs on a given day
     *
     * @param aDay the day to test
     * @return true if the event occurs on that day, false otherwise
     */
    @Override
    public boolean isInDay(LocalDate aDay) {
        boolean isInDay;
        LocalDate dateDoccurence=getStart().toLocalDate();

        do {
            if (super.dateExceptions.contains(aDay)) {
                isInDay=false;
                break;}


            LocalDate debut=dateDoccurence;
            LocalDate fin = debut.plusDays(super.getDuration().toDays());
            isInDay = ((aDay.isEqual(debut))||(aDay.isEqual(fin))) || (aDay.isAfter(debut) && aDay.isBefore(fin));
            if (isInDay) break;
            dateDoccurence= dateDoccurence.plusDays(getFrequency().getDuration().toDays());



        }
        while(ChronoUnit.DAYS.between (dateDoccurence,getTerminationDate())>=0);

        return isInDay;
    }
}
