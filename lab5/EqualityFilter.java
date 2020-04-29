/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    private String _match;
    private String _colName;
    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        this._match= match;
        this._colName= colName;
        // FIXME: Add your code here.
    }

    @Override
    protected boolean keep() {
        int position = super.headerList().indexOf(_colName);
        if(super.candidateNext().getValue(position).equals(_match)){
            return true;

        }
        return false;
        }
        // FIXME: Replace this line with your code.
    }

    // FIXME: Add instance variables?


