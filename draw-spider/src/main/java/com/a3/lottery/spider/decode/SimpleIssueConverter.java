package com.a3.lottery.spider.decode;

public class SimpleIssueConverter implements IssueConverter {

    private int pos;
    private int fix;

    public SimpleIssueConverter(int pos, int fix) {
        super();
        this.pos = pos;
        this.fix = fix;
    }

    @Override
    public String convert(String expect) {
        String result = expect;
        if (pos > 0) {
            String start = expect.substring(0, pos);
            String end = expect.substring(fix < 0 ? pos - fix : pos, expect.length());

            if (fix == 1) {
                end = '0' + end;
            }
            result = start + '-' + end;
        }
        return result;
    }

    @Override
    public String revert(String issueNo) {
        // TODO Auto-generated method stub
        return null;
    }

    public static void main(String[] args) {
        {
            SimpleIssueConverter converter = new SimpleIssueConverter(8, -1);
            String expect = "2021020401";

            System.out.println(converter.convert(expect));
        }

        {
            SimpleIssueConverter converter = new SimpleIssueConverter(8, 0);
            String expect = "2021020420";

            System.out.println(converter.convert(expect));
            ;
        }
        {
            SimpleIssueConverter converter = new SimpleIssueConverter(8, 1);
            String expect = "2021020401";

            System.out.println(converter.convert(expect));
            ;
            String formatCode=String.format("%02d", 1);
            System.out.println(formatCode);
        }
        
        
    }

}
