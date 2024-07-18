package top.cutexingluo.tools.utils.se.string;

/**
 * CPPString interface
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/20 19:20
 * @since 1.0.2
 */
public interface BaseCPPString {
    /**
     * CPP string::npos
     */
    int NPOS = -1;

    /**
     * get  the string
     */
    String getString();

    /**
     * cpp string size equals string length
     */
    int getSize();

    /**
     * string length for Java
     */
    default int length() {
        return getSize();
    }

    /**
     * sub string
     *
     * @param begIndex the beginning index , inclusive
     * @param len      the target length
     * @return new string
     */
    String substr(int begIndex, int len);

    /**
     * sub string
     *
     * @param begIndex the beginning index , inclusive
     * @return new string
     */
    String substr(int begIndex);

    /**
     * swap string value
     */
    void swap(BaseCPPString aString);

    /**
     * assign string value
     */
    BaseCPPString assign(String str);

    /**
     * assign string value
     *
     * @param begIndex the beginning index , inclusive
     * @param len      the target sub length
     */
    BaseCPPString assign(String str, int begIndex, int len);

    /**
     * assign string value
     */
    BaseCPPString assign(int count, char ch);

    /**
     * assign string value
     */
    BaseCPPString assign(int count, String str);

    /**
     * append string value to the end of the string
     */
    BaseCPPString append(String str);

    /**
     * append the sub string value to the end of the string
     */
    BaseCPPString append(String str, int begIndex, int len);

    /**
     * append several repeating characters to the end of the string
     */
    BaseCPPString append(int count, char ch);

    /**
     * appends several repeating strings to the end of the string
     */
    BaseCPPString append(int count, String str);


    //----------------------compare---------------------

    /**
     * compares two strings
     *
     * @param beginIndex  the beginning index
     * @param len         the subString length
     * @param str         the target string to compare
     * @param strBegIndex the beginning index of the target string
     * @param strLen      the subString length of the target string
     * @return <p>1)    0 if the strings are equal; </p>
     * <p>2)   a value less than 0 if the first string is less than the second string;</p>
     * <p>3)   a value greater than 0 if the first string is greater than the second string</p>
     */
    int compare(int beginIndex, int len, String str, int strBegIndex, int strLen);

    /**
     * compares two strings
     */
    int compare(int beginIndex, int len, String str, int strBegIndex);

    /**
     * compares two strings
     */
    int compare(int beginIndex, int len, String str);

    /**
     * compares two strings
     */
    int compare(String str);

    //----------------------find---------------------

    /**
     * Finds the first position of the substring in the string
     *
     * @param subString  the substring
     * @param startIndex the start index to find , inclusive
     * @return the position of the substring
     */
    int find(String subString, int startIndex);

    /**
     * Finds the first position of the substring in the string
     *
     * @param subString the substring
     * @return the position of the substring
     */
    int find(String subString);

    /**
     * Find the first substring's position in the string backwards
     * <p><b>Reverse lookup string. Positive match string</b> </p>
     * <p>Contains this position and the string following it.  </p>
     * <p>e.g. "bababa".rFind(2,"ba") = 2</p>
     *
     * @param subString       the substring
     * @param startIndexToPre the start index to find , Look forward from this position,  inclusive
     * @return the position of the substring
     */
    int rFind(String subString, int startIndexToPre);

    /**
     * Find the first substring's position in the string backwards
     * <p><b>Reverse lookup string. Positive match string</b> </p>
     *
     * @param subString the substring
     * @return the position of the substring
     */
    int rFind(String subString);

    /**
     * Finds the first position of any element in a character collection that exists in a string
     *
     * @param charCollection the characters' collection to search
     * @param startIndex     the start index to find , inclusive
     * @return the position of the any element
     */
    int findFirstOf(String charCollection, int startIndex);

    /**
     * Finds the first position of any element in a character collection that exists in a string
     *
     * @param charCollection the characters' collection to search
     * @return the position of the any element
     */
    int findFirstOf(String charCollection);

    /**
     * Finds the last position of any element in a character collection that exists in a string
     * <p><b>Reverse lookup string</b> </p>
     *
     * @param charCollection the characters' collection to search
     * @param startIndex     the start index to find , inclusive
     * @return the position of the any element
     */
    int findLastOf(String charCollection, int startIndex);

    /**
     * Finds the last position of any element in a character collection that exists in a string
     * <p><b>Reverse lookup string</b> </p>
     *
     * @param charCollection the characters' collection to search
     * @return the position of the any element
     */
    int findLastOf(String charCollection);

    /**
     * Finds the first position of all elements of a character set that does not exist in a string
     *
     * @param charCollection the characters' collection to search
     * @param startIndex     the start index to find , inclusive
     * @return the position
     */
    int findFirstNotOf(String charCollection, int startIndex);

    /**
     * Finds the first position of all elements of a character set that does not exist in a string
     *
     * @param charCollection the characters' collection to search
     * @return the position
     */
    int findFirstNotOf(String charCollection);

    /**
     * Finds the last position of all elements of a character set that does not exist in a string
     *
     * @param charCollection the characters' collection to search
     * @param startIndex     the start index to find , inclusive
     * @return the position
     */
    int findLastNotOf(String charCollection, int startIndex);


    /**
     * Finds the last position of all elements of a character set that does not exist in a string
     *
     * @param charCollection the characters' collection to search
     * @return the position
     */
    int findLastNotOf(String charCollection);

    //--------------------replace-------------------------

    /**
     * Replace the substring of the original string with a substring of target string
     *
     * @param beginIndex  original string begin index
     * @param len         substring length
     * @param str         target  string to replace
     * @param strBegIndex target string begin index
     * @param strLen      target  substring length
     * @return this BaseCPPString
     */
    BaseCPPString replace(int beginIndex, int len, String str, int strBegIndex, int strLen);

    /**
     * Replace the substring of the original string with a substring of target string
     *
     * @param beginIndex  original string begin index
     * @param len         substring length
     * @param str         target string to replace
     * @param strBegIndex target string begin index
     * @return this BaseCPPString
     */
    BaseCPPString replace(int beginIndex, int len, String str, int strBegIndex);

    /**
     * Replace the substring of the original string with  target string
     *
     * @param beginIndex original string begin index
     * @param len        substring length
     * @param str        target string to replace
     * @return this BaseCPPString
     */
    BaseCPPString replace(int beginIndex, int len, String str);

    /**
     * Replace the substring of the original string with a number of substring
     *
     * @param beginIndex original string begin index
     * @param len        substring length
     * @param count      target string count
     * @param str        target string (include character)
     * @return this BaseCPPString
     */
    BaseCPPString replace(int beginIndex, int len, int count, String str);

    //----------------------erase--------------------------

    /**
     * Deletes a substring of the specified length from a string
     *
     * @param beginIndex the beginning index, inclusive
     * @param len        the length to delete
     * @return this BaseCPPString
     */
    BaseCPPString erase(int beginIndex, int len);

    /**
     * Deletes a substring starting at a specified position from a string
     *
     * @param beginIndex the beginning index, inclusive
     * @return this BaseCPPString
     */
    BaseCPPString erase(int beginIndex);


    /**
     * Deletes a character from a string
     *
     * @param index the index of the character
     * @return this BaseCPPString
     */
    BaseCPPString eraseCharAt(int index);


    /**
     * Inserts a string from the specified position
     *
     * @param beginIndex the beginning index
     * @param str        the string to insert
     * @return this BaseCPPString
     */
    BaseCPPString insert(int beginIndex, String str);

    /**
     * Inserts a specified number of strings from a specified position
     *
     * @param beginIndex the beginning index
     * @param count      the number of target string to insert
     * @param str        the string to insert
     * @return this BaseCPPString
     */
    BaseCPPString insert(int beginIndex, int count, String str);

    /**
     * clear the string , means assign the string ""
     *
     * @return this BaseCPPString
     */
    BaseCPPString clear();
}
