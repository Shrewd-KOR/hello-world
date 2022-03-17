import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Solution {
    public static void main(String[] args) {

        String[] ledgers = new String[4];
        ledgers[0] = "04/01 1 40000";
        ledgers[1] = "05/01 5 20000";
        ledgers[2] = "08/31 4 10000";
        ledgers[3] = "11/11 0 -45000";

        double answer = 0;
        String tempSet = "";

        ArrayList<String> ledgerList = new ArrayList<>();
        for(String ledger : ledgers){
            ledgerList.add(ledger);
        }

        int listIndex = ledgerList.size()-1;

        //입출금내역으로 이자계산
        while(listIndex > 0)
        {
            // 공백으로 토큰 나누고
            String[] ledgerToken = ledgerList.get(listIndex).split("\\s");  // 출금 토큰

            if(Integer.parseInt(ledgerToken[2]) < 0) // 출금 금액이면
            {
                int balance = Math.abs(Integer.parseInt(ledgerToken[2]));  // 출금 금액중 정산할(남은) 금액

                for(int i=listIndex-1; i >= 0; i--)
                {
                    String[] Token = ledgerList.get(i).split("\\s");    // 입금 토큰
                    if(Integer.parseInt(Token[2]) > 0) // 입금 금액이면 정산
                    {

                        // 날짜 계산
                        String dateIn = Token[0];
                        String dateOut = ledgerToken[0];

                        Date formatIn = null;
                        Date formatOut = null;
                        try{
                            formatIn  = new SimpleDateFormat("MM/dd").parse(dateIn);
                            formatOut = new SimpleDateFormat("MM/dd").parse(dateOut);
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        long diffSec = (formatOut.getTime() - formatIn.getTime()) / 1000;
                        long diffDays = diffSec / (24*60*60);


                        // 입금액이 정산할금액보다 같거나 작으면(계속 정산)
                        if( balance >= Integer.parseInt(Token[2]) )
                        {
                            // 이자 계산(입금액만큼)
                            int rate = 0;
                            double rateP = (Double.parseDouble(Token[1]) / 100);

                            answer += Math.floor( (Double)((Integer.parseInt(Token[2]) * rateP) * (diffDays / 365.0)));

                            // --------------------------
                            //Ledger 리스트에서 입금 토큰 제거
                            //ledgerList.remove(i);
                            // ======> 오류1) 제거 대신에 금액 0 만들기
                            // ======> 오류2)
                            tempSet = Token[0]+" "+Token[1]+" "+Integer.toString(0);
                            ledgerList.set(i, tempSet);

                            //입금액만큼 정산
                            balance = balance - Integer.parseInt(Token[2]);

                        }
                        // 입금액이 정산할금액보다 크면(출금 정산 완료)
                        else
                        {
                            // 이자 계산(남은 잔액만큼)
                            int rate = 0;
                            double rateP = Double.parseDouble(Token[1]) / 100;


                            answer += Math.floor( (Double)((balance * rateP) * (diffDays / 365.0)) );

                            // --------------------------
                            //Ledger 리스트에서 입금 토큰 유지하되 정산한만큼 입금액에서 빼기
                            //ledgerList.set(i, Integer.toString(Integer.parseInt(Token[2])-balance) );
                            // ======> 오류2)
                            tempSet = Token[0]+" "+Token[1]+" "+Integer.toString(Integer.parseInt(Token[2])-balance);
                            ledgerList.set(i, tempSet);

                            // --------------------------
                            //Ledger 리스트에서 출금 토큰 제거하고
                            //ledgerList.remove(listIndex);
                            // ======> 오류1) 제거 대신에 금액 0 만들기
                            // ======> 오류2)
                            tempSet = ledgerToken[0]+" "+ledgerToken[1]+" "+Integer.toString(0);
                            ledgerList.set(listIndex, tempSet);

                            // 정산 완료
                            balance = 0;
                            break;
                        }
                    }

                }
            }

            listIndex--;
        }

        System.out.println("입출금정산 후 이자 :"+answer);

        System.out.println("12월31일 정산");

        //12월31일에 정산
        listIndex = ledgerList.size()-1;
        //System.out.println("ledgerList.size() :"+ledgerList.size());
        while(listIndex >= 0)
        {
            // 공백으로 토큰 나누고
            String[] ledgerToken = ledgerList.get(listIndex).split("\\s");  // 입금 토큰


            //System.out.println("Integer.parseInt(ledgerToken["+listIndex+"]) :"+ledgerToken[2]);

            if(Integer.parseInt(ledgerToken[2]) > 0 ) {
                int balance = Math.abs(Integer.parseInt(ledgerToken[2]));  // 입금 금액중 정산할(남은) 금액

                System.out.println("Balance :"+balance);

                // 날짜 계산
                String dateIn = ledgerToken[0];
                //System.out.println("dateIn : "+dateIn);
                String dateOut = "12/31";

                Date formatIn = null;
                Date formatOut = null;
                try {
                    formatIn = new SimpleDateFormat("MM/dd").parse(dateIn);
                    formatOut = new SimpleDateFormat("MM/dd").parse(dateOut);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                long diffSec = (formatOut.getTime() - formatIn.getTime()) / 1000;
                long diffDays = diffSec / (24 * 60 * 60);

                //System.out.println("diffDays : "+diffDays);

                // 이자 계산(남은 잔액만큼)
                int rate = 0;
                double rateP = Double.parseDouble(ledgerToken[1]) / 100;

                //System.out.println("rateP :"+rateP);
                System.out.println("12/31 이자: "+Math.floor((balance * rateP) * (diffDays / 365.0)));

                answer += Math.floor((balance * rateP) * (diffDays / 365.0));
                //System.out.println("현재 이자: "+answer);
            }
            listIndex--;
        }

        System.out.println(answer);
    }
}
