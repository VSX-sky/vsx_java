package vsx.testcalc;

import java.io.IOException;
import java.util.*;
import static vsx.testcalc.Main.*;

/** Объект  класса {@code Main} содержит управляющую логику программы, осуществляет анализ введенного
 * строкового математического выражения, передачу данных экземплярам объектам класса {@code SourceNum},
 * инициацию арифметической операции, вывод результата в консоль
 * @author Evgeny Gorbachev
 * @version 1.0
 */

public class Main {

    /** Объект {@code Scanner} служит для приема вводимой в консоль строки с выражением
     *
     */
    public static Scanner inUser;
    /** Константа {@code MAC_OPERANDS} задает максимальное количество чисел в выражении. В текущей версии
     * программы будет работать только MAX_OPERANDS = 2, при необходимости функционал может быть расширен
     */
    public static final int MAX_OPERANDS = 2;
    /** Константа {@code MIN_SOURCENUM} задает минимальное значение числа, которое может быть в выражении
     *
     */
    public static final int MIN_SOURCENUM = 1;
    /** Константа {@code MAN_SOURCENUM} задает максимальное значение числа, которое может быть в выражении
     *
     */
    public static final int MAX_SOURCENUM = 10;

    public static final String ErrMsg_01 = "Ошибка 01: Введена пустая строка!";
    public static final String ErrMsg_02 = "Ошибка 02: Первый символ не опознан!";
    public static final String ErrMsg_03 = "Ошибка 03: Символ не опознан!";
    public static final String ErrMsg_04 = "Ошибка 04: Превышено предельное количество агрументов!";
    public static final String ErrMsg_05 = "Ошибка 05: Не допускаются совместные операции с арабскими и римскими числами!";
    public static final String ErrMsg_06 = "Ошибка 06: Не введена арифметическая операция!";
    public static final String ErrMsg_07 = "Ошибка 07: Результирующее римское число не может быть меньше I!";
    public static final String ErrMsg_08 = "Ошибка 08: Недопустимое римское число ";
    public static final String ErrMsg_09 = "Ошибка 09: Входящее число превысило максимальную величину ";
    public static final String ErrMsg_10 = "Ошибка 10: Недопустимое входящее число ";

    /** Метод {@code main}
     * Обеспечивает прием данных с консоли, их передачу методу {@code calc} осуществляющему проверку введенных
     * данных и расчеты, а также выводит в консоль результаты расчетов
     * @param args не используется, возможно расширить функционал для ввода математических операций из
     *             командной строки
     * @throws IOException исключения могут выбрасываться при обращении к методу {@code calc}
     */

    public static void main(String[] args) throws IOException {
        System.out.println("Строковой калькулятор. Автор Горбачев Евгений. Версия 1.0");
        System.out.println("Введите в одну строку выражение из чисел от " + MIN_SOURCENUM + " до "
                + MAX_SOURCENUM);
        System.out.println("Поддерживаемые операции: +, -, *, /");
        System.out.println("Завершите ввод клавишей Enter");
        System.out.println("");
        inUser = new Scanner (System.in);

        System.out.println("Результат вычисления: " + calc(inUser.nextLine()));
    }

    /** Метод {@code calc}
     * Проверяет и обрабатывает строковое выражение, содержащее потенциальную математическую операцию. Создает
     * массив объектов {@code SourceNum}, содеражщих члены математических выражений и применяемые к ним операторы.
     * Инициирует операции между числами и возвращает результат в метод {@code main}.
     * @param input входящее строковое выражение, которое подлежит проверке и преобразованию в математическую
     *              операцию
     * @return возвращает результат расчетов в формате String
     * @throws IOException исключения выбрасываются при неверном вводе данных: пустая строка вместо выражения,
     * ввод неподходящего символа, превышаения заданного в программе лимита на количество аргументов выражения
     */
    public static String calc(String input) throws IOException {
        char CurrentChar;
        enum AcceptedSymbols {I,V,X}
        AcceptedSymbols acceptedSymbols = null;
        SourceNum[] SourceNumber = new SourceNum[MAX_OPERANDS];
        int indexSN = 0;

        if (input.isEmpty()) {
            System.out.println(ErrMsg_01);
            throw new IOException();
        }
        CurrentChar = input.charAt(0);
        if (Character.isDigit(CurrentChar)) {
            for (int i = 0; i < MAX_OPERANDS; i++) {
                SourceNumber[i] = new SourceNum(true);
            }
        }
        else{
            try {
                acceptedSymbols = Enum.valueOf(AcceptedSymbols.class, String.valueOf(CurrentChar));
                for (int i = 0; i < MAX_OPERANDS; i++) {
                    SourceNumber[i] = new SourceNum(false);
                    }
                }
            catch (IllegalArgumentException exception) {
                System.out.println(ErrMsg_02);
                throw new IOException();
            }
        }
        for (int i = 0; i < input.length(); i++) {
            CurrentChar = input.charAt(i);
            if (Character.isDigit(CurrentChar)){
                SourceNumber[indexSN].AddSymbol(String.valueOf(CurrentChar));
            }
            else if (Character.isWhitespace(CurrentChar)) { // пропускаем пробелы
            }
            else if (String.valueOf(CurrentChar).equals("+")){
                SourceNumber[indexSN].AttachOperand(1);
                indexSN++;
            }
            else if (String.valueOf(CurrentChar).equals("-")){
                SourceNumber[indexSN].AttachOperand(2);
                indexSN++;
            }
            else if (String.valueOf(CurrentChar).equals("*")){
                SourceNumber[indexSN].AttachOperand(3);
                indexSN++;
            }
            else if (String.valueOf(CurrentChar).equals("/")){
                SourceNumber[indexSN].AttachOperand(4);
                indexSN++;
            }
            else{
                try{
                   acceptedSymbols = Enum.valueOf(AcceptedSymbols.class, String.valueOf(CurrentChar));
                   SourceNumber[indexSN].AddSymbol(String.valueOf(CurrentChar));
                }
                catch (IllegalArgumentException exception) {
                        System.out.println(ErrMsg_03);
                        throw new IOException();
                    }
                }
            if (indexSN == MAX_OPERANDS){
                System.out.println(ErrMsg_04);
                throw new IOException();
            }

            }
        return SourceNumber[0].Invoke(SourceNumber[1]);
    }
}

    /** Класс {@code SourceNum} содержит сведения о составляющих математического выражения:
    *  числах и операторах. В классе осуществляются проверки на допустимость аргументов
    * @author Evgeny Gorbachev
    * @version 1.0
    */
class SourceNum {

    private StringBuilder NumBuffer = new StringBuilder();
    private int Operand = 0;
    private boolean isArabian;

    /** Конструктор {@code SourceNum(boolean isArabian)} создает объект класса с определением его
     * системы счисления
     * @param isArabian выбирает систему счисления для создаваемого объекта (Арабская или Римская), где
     *                  true - арабское число, false - римское число
     */
    SourceNum(boolean isArabian){
        this.isArabian=isArabian;
    }

    /** Метод {@code void AddSymbol(String Symbol)} добавляет символ числа к объекту, проверяя на соответствие
    * выбранной системе счисления. Выбрасывыает исключение при несоответствии
    * @param Symbol значение символа, которое необходимо проверить и присоединить к создаваемому числу
    * @throws IOException выбрасывает исключения при попытке использования Римских и Арабских чисел в одном
    * выражении
    */
    void AddSymbol(String Symbol) throws IOException {
        if (isArabian){
            if (Character.isDigit(Symbol.charAt(0))){
                this.NumBuffer.append(Symbol);
            }
            else {
                System.out.println(ErrMsg_05);
                throw new IOException();
            }
        }
        else{
            if (!Character.isDigit(Symbol.charAt(0))){
                this.NumBuffer.append(Symbol);
            }
            else {
                System.out.println(ErrMsg_05);
                throw new IOException();
            }

        }
    }

    /** Метод {@code void AttachOperand(int Operand)} прикрепляет к объекту математический оператор
    *
    * @param Operand код математического оператора: 0 - операция не назначена, 1 - сложение "+",
    *                2 - вычитание "-", 3 - умножение "*", 4 - деление "/"
    */
    void AttachOperand(int Operand){
        this.Operand=Operand;
    }

    /** Метод {@code String Invoke(SourceNum NextNum)} выполняет прикрепленную к данному экземпляру
    * математическую операцию. Вторую часть выражения получаем по значению при вызове метода.
    * Возвращаем строку с результатом или выбрасывает исключение
    * @param NextNum ссылка на Объект класса {@code SourceNum} который будет использован как второй аргумент
    *                в выполняемой арифместической операции
    * @return искомое результирующее число арифместической операции, приведенной к требуемой системе счисления.
    * @throws IOException выбрасывает исключения при отсутствии математического оператора или несоответсвия
    * результата приемлемому диапазону (полученное Римское число меньше I)
    */
    String Invoke(SourceNum NextNum) throws IOException {
        int result;
        String output;
            switch (this.Operand){
                case 1:
                    result = this.FinishNumber() + NextNum.FinishNumber();
                    break;
                case 2:
                    result = this.FinishNumber() - NextNum.FinishNumber();
                    break;
                case 3:
                    result = this.FinishNumber() * NextNum.FinishNumber();
                    break;
                case 4:
                    result = this.FinishNumber() / NextNum.FinishNumber();
                    break;
                default:
                    System.out.println(ErrMsg_06);
                    throw new IOException();
            }

        if (isArabian) output = String.valueOf(result);
        else{
            output = FormatNum.ArabToRome(result);
        }
        if (output == null){
            System.out.println(ErrMsg_07);
            throw new IOException();
        }
        return output;
    }

    /** Метод {@code private int FinishNumber()} преобразует поле {@code StringBuilder NumBuffer} в число,
    * проверяет его соответствие заданному диапазону входных данных и возвращает аргумент в формате int
    */
    private int FinishNumber() throws IOException {
        int output = 0;
        if (isArabian){
            output = Integer.parseInt(NumBuffer.toString());
        }
        else{
            output = FormatNum.RomeToArab(NumBuffer.toString());
            if (output == 0) {
                System.out.println(ErrMsg_08 + NumBuffer.toString() + " !");
                throw new IOException();
            }

        }
        if (output > MAX_SOURCENUM) {
            System.out.println(ErrMsg_09 + NumBuffer.toString() + " !");
            throw new IOException();
        }
        if (output < MIN_SOURCENUM) {
            System.out.println(ErrMsg_10 + NumBuffer.toString() + " !");
            throw new IOException();
        }
        return output;
    }
}

/** Класс {@code FormatNum} реализует конвертер из Римских числе в Арабские и наоборот. Содержит только
 * статичные поля и методы
 * @author Evgeny Gorbachev
 * @version 1.0
 */
class FormatNum{

    private static String[] RUnits = {"","I","II","III","IV","V","VI","VII","VIII","IX"};
    private static String[] RDozens = {"","X","XX","XXX","XL","L","LX","LXX","LXXX","XC","C"};
    private static String[] ComplianceTable = new String[101];

    static {
        for (int i = 1; i < 101; i++) {
              ComplianceTable[i] = RDozens[i/10] + RUnits[i%10];
        }
    }

    /** Статический метод {@code RomeToArab} преобразует заданное Римское число в Арабское
     *
     * @param RNum входящее Римское число в формате String
     * @return возвращает Арабское число для формате int.
     */
    static int RomeToArab(String RNum){
        for (int i = 1; i < 101; i++) {
            if (ComplianceTable[i].equals(RNum)) return i;
        }
        return 0;
    }

    /** Статический метод {@code ArabToRome} преобразует заданное Арабское число в Римское
     *
     * @param ANum входящее Арабское число для формате int.
     * @return возвращает Римское число в формате String
     * @throws IOException выбрасывает исключение если получается Римское число меньше I.
     */
    static String ArabToRome(int ANum) throws IOException {
        try{
            return ComplianceTable[ANum];
        }
        catch (ArrayIndexOutOfBoundsException exception){
            System.out.println(ErrMsg_07);
            throw new IOException();
        }
    }

}