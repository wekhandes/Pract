# Transaction Aggregator

Clone the repo in IDE and run the main function.

This will print the aggregation and will also write the results to the CSV file.

Input CSV file path has been set to D:\code\input\transactions.csv and code will write the output files to D:\code\input folder.

File path and names can be updated in TransactionService class

### Problem Statement

Given a list of Debits and credits for the merchant Moonbucks, find the sum of all credits and debits for the different combinations provided below.

- AccountCode
- ActionDate, AccountCode
- ActionDate, AccountCode, AccountType

A credit is represented by the code ‘C’ which needs to be treated as positive and Debit with the code ‘D’ as negative.

Sample CSV file is attached which can be used as an input.

Input CSV file:

```csv
Merchant,PaymentId,Date,TransactionId,TransactionTs,Stage,Method,MethodCode,AccountCode,ServiceId,ServiceCategory,AccountType,CreditDebitCode,Amount,Currency,Decimal,Version,ActionDate,EntryDate,BatchId,Entity
MOONBUCKS,PAYOUT1,20220714,00000000-df39-4643-b782-ca441dadc3c1,2022-05-18T16:08:16.612Z,Queued,SCHEME1,NI,MERCHANT,SBK00,Billed,AP,C,1908220500,GBP,8,\N,2022-02-23,2022-05-27,\N,LE1
MOONBUCKS,\N,20220714,00000000-df39-4643-b782-ca441dadc3c1,2022-05-18T16:08:16.612Z,Queued,SCHEME1,NI,MERCHANT,SBK00,Billed,R,C,1908220500,GBP,8,\N,2022-02-23,2022-05-27,\N,LE1
MOONBUCKS,PAYOUT1,20220714,00000000-df39-4643-b782-ca441dadc3c1,2022-05-18T16:08:16.612Z,Queued,SCHEME1,NI,INCOME,SBK00,Billed,R,C,1908220500,GBP,8,\N,2022-02-23,2022-05-27,\N,LE1
```

Aggregation input:

```
ActionDate,AccountCode
```

Output CSV file/Logger/System Out:

```csv
ActionDate,AccountCode,Amount,Decimal
2022-02-23,MERCHANT,3816441000,8
2022-02-23,INCOME,1908220500,8
```
