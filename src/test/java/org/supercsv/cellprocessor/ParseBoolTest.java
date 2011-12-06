package org.supercsv.cellprocessor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the ParseBool processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class ParseBoolTest {
	
	private static final String TRUE_VALUE = "y";
	private static final String FALSE_VALUE = "n";
	private static final String[] DEFAULT_TRUE_VALUES = new String[] { "1", "true", "t", "y" };
	private static final String[] DEFAULT_FALSE_VALUES = new String[] { "0", "false", "f", "n" };
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processor3;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	private CellProcessor processorChain3;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new ParseBool();
		processor2 = new ParseBool(TRUE_VALUE, FALSE_VALUE);
		processor3 = new ParseBool(new String[] { TRUE_VALUE }, new String[] { FALSE_VALUE });
		processorChain = new ParseBool(new IdentityTransform());
		processorChain2 = new ParseBool(TRUE_VALUE, FALSE_VALUE, new IdentityTransform());
		processorChain3 = new ParseBool(new String[] { TRUE_VALUE }, new String[] { FALSE_VALUE },
			new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with valid true/false values.
	 */
	@Test
	public void testValidInput(){
		
		// processors using default true/false values
		for (String trueValue : DEFAULT_TRUE_VALUES){
			assertTrue((Boolean) processor.execute(trueValue, ANONYMOUS_CSVCONTEXT));
			assertTrue((Boolean) processorChain.execute(trueValue, ANONYMOUS_CSVCONTEXT));
		}
		for (String falseValue : DEFAULT_FALSE_VALUES){
			assertFalse((Boolean) processor.execute(falseValue, ANONYMOUS_CSVCONTEXT));
			assertFalse((Boolean) processorChain.execute(falseValue, ANONYMOUS_CSVCONTEXT));
		}
		
		// other processors with single supplied true/false values
		assertTrue((Boolean) processor2.execute(TRUE_VALUE, ANONYMOUS_CSVCONTEXT));
		assertTrue((Boolean) processor3.execute(TRUE_VALUE, ANONYMOUS_CSVCONTEXT));
		assertTrue((Boolean) processorChain2.execute(TRUE_VALUE, ANONYMOUS_CSVCONTEXT));
		assertTrue((Boolean) processorChain3.execute(TRUE_VALUE, ANONYMOUS_CSVCONTEXT));
		assertFalse((Boolean) processor2.execute(FALSE_VALUE, ANONYMOUS_CSVCONTEXT));
		assertFalse((Boolean) processor3.execute(FALSE_VALUE, ANONYMOUS_CSVCONTEXT));
		assertFalse((Boolean) processorChain2.execute(FALSE_VALUE, ANONYMOUS_CSVCONTEXT));
		assertFalse((Boolean) processorChain3.execute(FALSE_VALUE, ANONYMOUS_CSVCONTEXT));
		
	}
	
	/**
	 * Tests execution with an non-boolean String input (should throw an exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testWithNonBooleanString() {
		processor.execute("not a boolean!", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a non-String input (should throw an exception).
	 */
	@Test(expected = ClassCastInputCSVException.class)
	public void testWithNonString() {
		processor.execute(1, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with an empty-String input (should throw an exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testWithEmptyString() {
		processor.execute("", ANONYMOUS_CSVCONTEXT);
	}

}
