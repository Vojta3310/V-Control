/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl;

/**
 *
 * @author vojta3310
 */
public interface ICommand {

  String GetFor();

  String GetFrom();

  String GetCommand();

  String GetParms();
}
