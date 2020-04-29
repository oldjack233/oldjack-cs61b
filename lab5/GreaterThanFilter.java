import java.util.HashSet;
import java.util.Iterator;

/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        // FIXME: Add your code here.
        this._colName = colName;
        this._input = input;
        this._ref = ref;
    }

    @Override
    protected boolean keep() {
        int postion = super.headerList().indexOf(_colName);
        String capa = super.candidateNext().getValue(postion);
        if(capa.length()> _ref.length()){
            return true;
        }
        else{
        // FIXME: Replace this line with your code.
        return false;
    }
    }

    // FIXME: Add instance variables?
    private String _colName;
    private String _ref;
    private Table _input;


    }

