package com.example.Demo_AI.aiTools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTool {
    @Tool(
            description = """
                 perform arithmetic calculations .
                 Supported operation: add, subtract, multiply, divide, mod, power
                 """
    )
    public double calculate(
            @ToolParam(description = "operation : add,subtract,multiply,divide,mod,power")
            String operation,
            @ToolParam(description = "first number ")
            double a,
            @ToolParam(description = "second number")
            double b){
        System.out.println("Calculator Tool called");
        if(operation.equals("add")){
            return a + b;
        }
        else if(operation.equals("subtract")){
            return a - b;
        }
        else if(operation.equals("divide")){
            if(b == 0){
                throw new IllegalArgumentException("Cannot divide by 0");
            }
            return a / b;
        }
        else if(operation.equals("multiply")){
            return a * b;
        }
        else if(operation.equals("mod")){
            if(b == 0){
                throw new IllegalArgumentException("Cannot calculate mod by 0");
            }
            return a % b;
        }
        else if(operation.equals("power")){
            return Math.pow(a, b);
        }
        return 0;
    }
}