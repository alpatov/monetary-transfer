### Mone Transfer REST API 

#### Design considerations

The best solution should be built on the basis of static dependency injection and compile time annotation processing. But for the moment there is no such Framework at the market.

After several considerations, the JAX-RS standard was recognized as the best choice for the implementation of Java-based microservice. The decision was made according to following criteria:

- flexibility;
- maintainability;
- complexity;
- functional completeness.

It was decided to use Jersey Micro Framework, because it is a references implementation of the JAX-RS standard. 

Jersey drawbacks should also be mentioned. Main worry is about dynamic Dependency Injection. It worth moving to static Dependency Injection. Dagger 2 can be suggested as a best choice. But for now it could not be achieved without increasing overall build weight.

#### What was implemented

*REST API for monetary transfer*

Developed API is represented with single REST-endpoint which requires account numbers and debit for transaction to execute properly. 

```http
POST http://{host}:8080/transfer
```

Request accepts an Electronic Transfer as a JSON payload, which must conform to the following specification:

```json
"components": {
    "schemas": {
        "Account": {
            "title": "Account",
            "type": "object",
            "properties": {
                "number": {
                    "description": "Numeric account number",
                    "type": "string",
                    "minimumLength": 8
                }
            },
            "required": ["number"]
        },
        "ElectronicTransfer": {
            "title": "Electronic Transfer",
            "type": "object",
            "properties": {
                "payee": {
                    "description": "Depository account",
                    "type": "Account"
                },
                "payer": {
                    "description": "Withdrawal account",
                    "type": "Account"
                },
                "debit": {
                    "description": "Funds to transfer",
                    "type": "BigDecimal"
                }
            },
            "required": ["payee", "payer", "debit"]
        },
        "GeneralError": {
            "type": "object",
            "properties": {
                "error": {
                    "description": "Error type",
					"type": "string"
                },
                "description": {
                    "description": "Error details",
                    "type": "string"
                }
            },
            "required": ["error", "description"]
        }
    },
    "responses": {
        "Notfound": {
            "description": "Entity not found",
            "content": {
                "application/json": {
                    "$ref": "#/components/schemas/GeneralError"
                }
            }
        },
		"IllegalInput": {
            "description": "Input violates integrity constraints",
            "content": {
                "application/json": {
                    "$ref": "#/components/schemas/GeneralError"
                }
            }
        },
        "GeneralError": {
            "content": {
                "application/json": {
                    "$ref": "#/components/schemas/GeneralError"
                }
            }
        }
    }
}
```

The implementation also includes data integrity checks. Requests containing incorrect or corrupted data will result in BAD_REQUEST responses. Non-existing accounts will result in NOT_FOUND responses. Error information is sent to the user as a JSON payload in the body of the response .

*Unit Tests*

The developed tests are designed to control the basic functionality, and are intended to check following Test Cases:

1. Valid transaction will complete properly.
2. Invalid transaction would not succeed and will be treated as BAD_REQUEST:
   - transaction with debit greater that available amount;
   - transaction with equal withdrawal and deposit accounts;
   - transaction with malformed JSON-payload;
   - transactions with payload that does not comply to provided JSON schema.
3. Requests containing non-existent accounts will result in NOT_FOUND responses.

#### What was not implemented and Why

REST API for account management was not implemented because it was considered not related to the current task.

Concurrency tests were not developed due to lack of time. This was considered a minor flaw due to the purpose of the current task. But in real tasks it is important to make sure that simultaneous tasks performed in a different order will lead to the same result.
