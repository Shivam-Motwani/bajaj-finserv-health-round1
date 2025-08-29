# VIT Java Assignment – Question 2 (Reg No: 22bce2528)

## 📌 Problem
For each employee, count how many employees in the same department are younger (DOB later) and return details in the required format.  
Tables: DEPARTMENT, EMPLOYEE, PAYMENTS.

## ✅ Final SQL Query
```sql
SELECT 
    e1.EMP_ID,
    e1.FIRST_NAME,
    e1.LAST_NAME,
    d.DEPARTMENT_NAME,
    COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT
FROM EMPLOYEE e1
JOIN DEPARTMENT d 
    ON e1.DEPARTMENT = d.DEPARTMENT_ID
LEFT JOIN EMPLOYEE e2 
    ON e1.DEPARTMENT = e2.DEPARTMENT
   AND e2.DOB > e1.DOB
GROUP BY 
    e1.EMP_ID,
    e1.FIRST_NAME,
    e1.LAST_NAME,
    d.DEPARTMENT_NAME
ORDER BY e1.EMP_ID DESC;
```
