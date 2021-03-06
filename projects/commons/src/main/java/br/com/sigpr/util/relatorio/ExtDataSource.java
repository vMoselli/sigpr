package br.com.sigpr.util.relatorio;

import java.util.Collection;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;

@SuppressWarnings("rawtypes")
public class ExtDataSource extends JRAbstractBeanDataSource
{

	public ExtDataSource(final Collection beanCollection)
    {
        this(beanCollection, true);
    }

    public ExtDataSource(final Collection beanCollection, final boolean isUseFieldDescription)
    {
        super(isUseFieldDescription);
        data = null;
        iterator = null;
        currentBean = null;
        data = beanCollection;
        if(data != null)
        {
        	iterator = data.iterator();
        }
    }

    public boolean next()
    {
        boolean hasNext = false;
        if(iterator != null)
        {
            hasNext = iterator.hasNext();
            if(hasNext)
            {
                currentBean = iterator.next();
            }
        }
        return hasNext;
    }

    public Object getFieldValue(final JRField field)
        throws JRException
    {
    	final Object val = getFieldValue(currentBean, field);
    	if (val instanceof Collection) {
    		return new ExtDataSource((Collection)val);
    	}
    	return val;
    }

    public void moveFirst()
    {
        if(data != null)
        {
            iterator = data.iterator();
        }
    }

    private transient Collection data;
    private transient Iterator iterator;
    private transient Object currentBean;
}