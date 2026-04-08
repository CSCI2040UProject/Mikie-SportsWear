import { useState } from "react";

export default function UserGuide() {
    const [openIndex, setOpenIndex] = useState(null);

    // Write questions and answers here
    const faqs = [
        {
            question: "Q1: Am I able to make purchases from the catalog?",
            answer: "A: No, there is no payment or \"add to cart\" functionality. The catalog only displays items, and doesn't allow for direct purchases."
        },
        { question: "Q2: ", answer: "A: " },
        { question: "Q3: ", answer: "A: " },
    ];

    const toggleQuestion = (index) => {
        setOpenIndex(openIndex === index ? null : index);
    };

    return (
        <>
            <style>{`
                .faq {
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                }
                .question {
                    width: 60vw;
                    text-align: left;
                    padding: 10px;
                    padding-left: 3vw;
                    margin-bottom: 12px;
                    background-color: #EFEFEF;
                    border-radius: 2vw;
                    cursor: pointer;
                }
                .answer {
                    font-weight: default;
                }
            `}</style>

            <div className="faq">
                    <h1>Frequently Asked Questions</h1>
                    {faqs.map((faq, index) => (
                        <div key={index} className="question" onClick={() => toggleQuestion(index)}>
                        <p style={{ fontWeight: "bold" }}>{faq.question}</p>
                        {openIndex === index && (
                            <div className="answer">
                                <p>{faq.answer}</p>
                            </div>
                        )}
                      </div>
                    ))}
                  </div>
        </>
    )
}