/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        this._input = input;
        this._colName1 = colName1;
        this._colName2 = colName2;
        // FIXME: Add your code here.
    }

    @Override
    protected boolean keep() {
        int a = _input.colNameToIndex(_colName1);
        int b = _input.colNameToIndex(_colName2);
        if(super.candidateNext().getValue(a).equals(super.candidateNext().getValue(b))){
            return true;}
        else{
            return false;
        }
        // FIXME: Replace this line with your code.

    }

    // FIXME: Add instance variables?
    private String _colName1;
    private String _colName2;
    private Table _input;
}
