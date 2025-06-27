#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

#define MAX 5
int arr[MAX];
int front = 0;  
int rear  = 0;  

void enqueue(int);
int dequeue();
int peek();
void traverse();

int main() {
    int choice, num, x;

    while (1) {
        printf("\nEnter your choice:\n");
        printf("1 for Enqueue\n");
        printf("2 for Dequeue\n");
        printf("3 for Peek\n");
        printf("4 for Traverse\n");
        printf("5 for Exit\n");
        printf("Choice: ");
        if (scanf("%d", &choice) != 1) {
            fprintf(stderr, "Invalid input\n");
            exit(EXIT_FAILURE);
        }

        switch (choice) {
            case 1:
                printf("Enter the number you want to insert: ");
                if (scanf("%d", &num) != 1) {
                    fprintf(stderr, "Invalid input\n");
                    exit(EXIT_FAILURE);
                }
                enqueue(num);
                break;

            case 2:
                x = dequeue();
                if (x == INT_MIN)
                    printf("Queue is empty\n");
                else
                    printf("Popped element is %d\n", x);
                break;

            case 3:
                x = peek();
                if (x == INT_MIN)
                    printf("Queue is empty\n");
                else
                    printf("Front element is %d\n", x);
                break;

            case 4:
                traverse();
                break;

            case 5:
                printf("Exiting...\n");
                return 0;

            default:
                printf("Enter a valid number for choice\n");
        }
    }
    return 0;
}
void enqueue(int num) {
    int next = (rear + 1) % MAX;
    if (next == front) {
        return ;
    }
    arr[rear] = num;
    rear = next;
    return 1;
}

int dequeue() {
    if (front == rear) {
        return INT_MIN;
    }
    int x = arr[front];
    front = (front + 1) % MAX;
    return x;
}

int peek() {
    if (front == rear) {
        return INT_MIN;
    }
    return arr[front];
}

void traverse() {
    if (front == rear) {
        printf("Queue is empty\n");
        return;
    }
    int i = front;
    while (i != rear) {
        printf("%d\t", arr[i]);
        i = (i + 1) % MAX;
    }
    printf("\n");
}
