package contracts.order

import org.springframework.cloud.contract.spec.Contract



Contract.make {

        request {
            method POST()

            url "/api/v1/orders"

            headers {
                contentType("application/json")
            }

            body([
                    productId: null,
                    customerId: null,
                    shipping: null,
                    billing: null,
                    quantity: null,
                    paymentMethod: null,
            ])
        }

        response {
            status 400

            headers {
                contentType("application/problem+json")
            }

            body(
                    type: "/errors/invalid-fields",
                    title: "Invalid fields",
                    status: 400,
                    detail: "One or more fields are invalid",
                    instance: "/api/v1/orders",
                    fields: [
                            productId: "must not be null",
                            customerId: "must not be null",
                            shipping: "must not be null",
                            paymentMethod: "must not be null",
                            billing: "must not be null",
                            quantity: "must not be null"
                    ]
            )
        }
    }