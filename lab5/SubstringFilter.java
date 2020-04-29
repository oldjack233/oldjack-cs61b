/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        // FIXME: Add your code here.
        this._colName = colName;
        this._input = input;
        this._subStr = subStr;
    }

    @Override
    protected boolean keep() {
        int position = super.headerList().indexOf(_colName);
        String container = super.candidateNext().getValue(position);
        if(container.contains(_subStr)){
            return true;
        }
        else{
            return false;
        }
        // FIXME: Replace this line with your code.

    }

    // FIXME: Add instance variables?
    private Table _input;
    private String _colName;
    private String _subStr;
}
