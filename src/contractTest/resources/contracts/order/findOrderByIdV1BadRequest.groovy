package contracts.order

import org.springframework.cloud.contract.spec.Contract



Contract.make {

        request {
            method POST()

            url "/api/v1/orders"

            headers {
                contentType("application/vnd.order-with-product.v1+json")
            }

            body([
                    id: null,
                    customer: null,
                    totalItems: 2,
                    totalAmount: 41.98,
                    placedAt: null,
                    canceledAt: null,
                    paidAt: null,
                    readyAt: null,
                    status: "PLACED",
                    paymentMethod: "GATEWAY_BALANCE",
                    shipping: null,
                    billing: null,
                    items: null
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
                            id: "must not be blank",
                            customer: "must not be null"
                    ]
            )
        }
    }